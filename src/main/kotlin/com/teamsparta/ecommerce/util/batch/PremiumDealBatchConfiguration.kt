package com.teamsparta.ecommerce.util.batch

import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDeal
import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDealApply
import com.teamsparta.ecommerce.domain.premiumdeal.repository.PremiumDealApplyRepository
import com.teamsparta.ecommerce.domain.premiumdeal.repository.PremiumDealRepository
import com.teamsparta.ecommerce.domain.premiumdeal.service.PremiumDealApplyService
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
    private val premiumDealApplyRepository: PremiumDealApplyRepository,
    private val premiumDealApplyService: PremiumDealApplyService,
    private val premiumDealRepository: PremiumDealRepository
) {


    @Bean
    fun premiumDealJob(jobRepository: JobRepository,deleteStep:Step,applyDeleteStep: Step,premiumDealStep: Step): Job {
        return JobBuilder("premiumDealJob", jobRepository)
            .start(deleteStep)//특가삭제
            .next(premiumDealStep)//apply-> 특가
            .build()
    }

    @Bean
    fun premiumDealStep(jobRepository: JobRepository, transactionManger: PlatformTransactionManager): Step {
        return StepBuilder("premiumDealStep",jobRepository)
            .chunk<PremiumDealApply, PremiumDeal>(10,transactionManger)
            .reader(premiumDealApplyItemReader())
            .processor(premiumDealProcessor())
            .writer(premiumDealItemWriter())
            .build()
    }

    @Bean
    fun deleteStep(jobRepository: JobRepository, transactionManger: PlatformTransactionManager,deleteTasklet: Tasklet): Step {
        return StepBuilder("premiumDealStep",jobRepository)
            .tasklet(deleteTasklet,transactionManger)
            .build()
    }



    @Bean
    fun premiumDealApplyItemReader(): ItemReader<PremiumDealApply> {
        val now = LocalDateTime.now()
        val startTime = now.minusDays(1).withHour(7).withMinute(1)
        val endTime = now.withHour(6).withMinute(59)

        return RepositoryItemReaderBuilder<PremiumDealApply>()
            .name("premiumDealApplyItemReader")
            .repository(premiumDealApplyRepository)
            .methodName("findByApplicationDateBetween")
            .arguments(listOf(startTime, endTime))
            .sorts(mapOf("id" to Direction.ASC))
            .build()
    }


    @Bean
    fun premiumDealProcessor(): ItemProcessor<PremiumDealApply, PremiumDeal> {
        return ItemProcessor { apply ->
            premiumDealApplyService.createPremiumDeal(apply)
        }
    }

    @Bean
    fun premiumDealItemWriter(): ItemWriter<PremiumDeal> {
        return ItemWriter { deals ->
            deals.forEach { deal ->
                premiumDealApplyService.save(deal)
            }
        }
    }
    @Bean
    fun applyDeleteTasklet(): Tasklet {
        return Tasklet { _, _ ->
            premiumDealApplyRepository.deleteAll()
            RepeatStatus.FINISHED
        }
    }
    @Bean
    fun deleteTasklet(): Tasklet {
        return Tasklet { _, _ ->
            premiumDealRepository.deleteAll()
            RepeatStatus.FINISHED
        }
    }
}
