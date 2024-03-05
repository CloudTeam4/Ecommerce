package com.teamsparta.ecommerce.util.batch

import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.job.flow.FlowExecutionStatus
import org.springframework.batch.core.job.flow.JobExecutionDecider
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class ConditionalStepExecutionDecider (private val memberRepository:MemberRepository): JobExecutionDecider {


    override fun decide(jobExecution: JobExecution, stepExecution: StepExecution?): FlowExecutionStatus {
        val currentDate = LocalDate.now()
        val targetDate = LocalDate.of(2024, 3, 1)
        return if (currentDate == targetDate) {
            FlowExecutionStatus("COMPLETED")
        } else {
            FlowExecutionStatus("FAILED")
        }
    }
}
