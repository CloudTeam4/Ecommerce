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
    private val premiumDealJob: Job
) {

//    @Scheduled(cron = "0 0 7 * * *") // 매일 07시에 실행
    @Scheduled(cron = "20 32 11 13 * *")
    fun runPremiumDealJob() {
        try {
            val jobParameters = JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters()
            jobLauncher.run(premiumDealJob, jobParameters).also {
                println("PremiumDeal job started with parameters: $jobParameters")
            }
        } catch (e: Exception) {
            println("Error occurred during execution of PremiumDeal job")
            e.printStackTrace()
        }
    }
}
