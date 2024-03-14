package com.teamsparta.ecommerce.util.batch

import com.teamsparta.ecommerce.domain.coupon.service.CouponService
import com.teamsparta.ecommerce.util.enum.Role
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableBatchProcessing
class BatchConfiguration(
    private val couponService: CouponService) {
    @Bean
    fun conditionalJob(jobRepository: JobRepository, step1: Step): Job {
        return JobBuilder("conditionalJob", jobRepository)
            .start(step1)
            .build()
    }

    @Bean
    fun step1(jobRepository: JobRepository,tasklet1: Tasklet,transactionManger:PlatformTransactionManager): Step {
        return StepBuilder("step1",jobRepository)
            .tasklet(tasklet1, transactionManger)
            .build()
    }

    @Bean
    fun tasklet1(): Tasklet {
        return Tasklet { _, _ ->
            couponService.assignCouponToUsers(Role.PREMIUM,4)
            RepeatStatus.FINISHED
        }
    }
}
