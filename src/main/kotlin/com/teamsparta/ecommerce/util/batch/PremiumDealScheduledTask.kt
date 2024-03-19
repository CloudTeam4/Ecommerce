package com.teamsparta.ecommerce.util.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class PremiumDealScheduledTask(
    private val jobLauncher: JobLauncher,
    private val eventJob: Job
) {

//    @Scheduled(cron = "0 0 7 * * *") // 매일 07시에 실행
    @Scheduled(cron = "00 39 13 18 * *")
    fun runEventJob() {
        try {
            val jobParameters = JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters()
            jobLauncher.run(eventJob, jobParameters).also {
                println("Event job started with parameters: $jobParameters")
            }
        } catch (e: Exception) {
            println("Error occurred during execution of Event job")
            e.printStackTrace()
        }
    }
}
