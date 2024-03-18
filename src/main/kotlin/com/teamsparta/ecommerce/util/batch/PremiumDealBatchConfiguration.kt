package com.teamsparta.ecommerce.util.batch

import com.teamsparta.ecommerce.domain.event.model.Event
import com.teamsparta.ecommerce.domain.event.model.EventApply
import com.teamsparta.ecommerce.domain.event.repository.EventApplyRepository
import com.teamsparta.ecommerce.domain.event.repository.EventRepository
import com.teamsparta.ecommerce.domain.event.service.EventApplyService
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort.Direction
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDateTime

@Configuration
@EnableBatchProcessing
class PremiumDealBatchConfiguration(
    private val eventApplyRepository: EventApplyRepository,
    private val eventApplyService: EventApplyService,
    private val eventRepository: EventRepository
) {


    @Bean
    fun EventJob(jobRepository: JobRepository,deleteStep:Step,EventStep: Step): Job {
        return JobBuilder("EventJob", jobRepository)
            .start(deleteStep)//특가삭제
            .next(EventStep)//apply-> 특가
            .build()
    }

    @Bean
    fun EventStep(jobRepository: JobRepository, transactionManger: PlatformTransactionManager): Step {
        return StepBuilder("EventStep",jobRepository)
            .chunk<EventApply, Event>(10,transactionManger)
            .reader(EventApplyItemReader())
            .processor(EventProcessor())
            .writer(EventItemWriter())
            .build()
    }

    @Bean
    fun deleteStep(jobRepository: JobRepository, transactionManger: PlatformTransactionManager,deleteTasklet: Tasklet): Step {
        return StepBuilder("EventStep",jobRepository)
            .tasklet(deleteTasklet,transactionManger)
            .build()
    }



    @Bean
    fun EventApplyItemReader(): ItemReader<EventApply> {
        val now = LocalDateTime.now()
        val startTime = now.minusDays(1).withHour(7).withMinute(1)
        val endTime = now.withHour(13).withMinute(59)

        return RepositoryItemReaderBuilder<EventApply>()
            .name("EventApplyItemReader")
            .repository(eventApplyRepository)
            .methodName("findByApplicationDateBetween")
            .arguments(listOf(startTime, endTime))
            .sorts(mapOf("id" to Direction.ASC))
            .build()
    }


    @Bean
    fun EventProcessor(): ItemProcessor<EventApply, Event> {
        return ItemProcessor { apply ->
            eventApplyService.createEvent(apply)
        }
    }

    @Bean
    fun EventItemWriter(): ItemWriter<Event> {
        return ItemWriter { deals ->
            deals.forEach { deal ->
                eventApplyService.save(deal)
            }
        }
    }
    @Bean
    fun deleteTasklet(): Tasklet {
        return Tasklet { _, _ ->
            eventRepository.deleteAll()
            RepeatStatus.FINISHED
        }
    }
}
