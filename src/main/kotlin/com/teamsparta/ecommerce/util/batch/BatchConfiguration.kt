package com.teamsparta.ecommerce.util.batch

import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.util.enum.Role
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.job.flow.FlowExecutionStatus
import org.springframework.batch.core.job.flow.JobExecutionDecider
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableBatchProcessing
class BatchConfiguration(private val memberRepository: MemberRepository
) {

    @Bean
    fun conditionalJob(jobRepository: JobRepository, decider: JobExecutionDecider, step1: Step, step2: Step): Job {
        return JobBuilder("conditionalJob", jobRepository)
            .start(step1)
            .next(decider).on("FAILED").to(step2)
            .from(decider).on("*").to(step1)
            .end()
            .build()

    }



    @Bean
    fun step1(jobRepository: JobRepository,tasklet1: Tasklet,transactionManger:PlatformTransactionManager): Step {
        return StepBuilder("step1",jobRepository)
            .tasklet(tasklet1, transactionManger)
            .build()
    }

    @Bean
    fun step2(jobRepository: JobRepository,tasklet2: Tasklet,transactionManger:PlatformTransactionManager): Step {
        return StepBuilder("step2",jobRepository)
            .tasklet(tasklet2(), transactionManger)
            .build()
    }

    @Bean
    fun tasklet1(): Tasklet {
        return Tasklet { _, _ ->
            println("This is step 1")
            RepeatStatus.FINISHED
        }
    }

    @Bean
    fun tasklet2(): Tasklet {
        return Tasklet { _, _ ->
            println("This is step 2")
            RepeatStatus.FINISHED
        }
    }

    @Bean
    fun decider(): JobExecutionDecider {
        return JobExecutionDecider { _, _ ->
            val allMembers = memberRepository.findAll()
            val hasUserRole = allMembers.any { it.role == Role.PREMIUM  }
            if (hasUserRole) {
                FlowExecutionStatus("FAILED")
            } else {
                FlowExecutionStatus("COMPLETED")
            }
        }
    }
}
