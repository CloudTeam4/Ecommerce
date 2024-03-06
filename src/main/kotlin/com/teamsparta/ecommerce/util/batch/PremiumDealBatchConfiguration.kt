package com.teamsparta.ecommerce.util.batch

import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDeal
import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDealApply
import com.teamsparta.ecommerce.domain.premiumdeal.repository.PremiumDealApplyRepository
import com.teamsparta.ecommerce.domain.premiumdeal.service.PremiumDealApplyService
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort.Direction
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableBatchProcessing
class PremiumDealBatchConfiguration(
    private val premiumDealApplyRepository: PremiumDealApplyRepository,
    private val premiumDealApplyService: PremiumDealApplyService
) {


    @Bean
    fun premiumDealJob(jobRepository: JobRepository, premiumDealStep: Step): Job {
        return JobBuilder("premiumDealJob", jobRepository)
            .start(premiumDealStep)
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
    fun premiumDealApplyItemReader(): ItemReader<PremiumDealApply> {
        return RepositoryItemReaderBuilder<PremiumDealApply>()
            .name("premiumDealApplyItemReader")
            .repository(premiumDealApplyRepository)
            .methodName("findAll")
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
}
