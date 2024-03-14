package com.teamsparta.ecommerce.util.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDate
import org.springframework.batch.core.JobParametersBuilder

    @Component
    class JobScheduler(
        private val jobLauncher: JobLauncher,
        private val jobs: Map<String, Job>
    ) : CommandLineRunner {

        override fun run(vararg args: String?) {
            val currentDate = LocalDate.now()
            val targetDate = LocalDate.of(2024, 3, 14)

            // 조건 확인 로직을 메서드로 추출하여 더 많은 조건을 유연하게 추가 가능
            if (shouldRunJob(currentDate, targetDate)) {
                val job = jobs["conditionalJob"]
                job?.let {
                    // 현재 시간을 밀리초 단위로 파라미터에 추가
                    val jobParameters = JobParametersBuilder()
                        .addLong("currentTime", System.currentTimeMillis())
                        .toJobParameters()

                    val jobExecution = jobLauncher.run(it, jobParameters)
                    println("JobExecution status: ${jobExecution.status}")
                }
            } else {
                println("조건에 맞지 않아 Job을 실행하지 않습니다.")
            }
        }

        // 날짜 또는 다른 조건에 따라 작업을 실행할지 결정하는 메서드
        private fun shouldRunJob(currentDate: LocalDate, targetDate: LocalDate): Boolean {
            return currentDate == targetDate
        }
    }

