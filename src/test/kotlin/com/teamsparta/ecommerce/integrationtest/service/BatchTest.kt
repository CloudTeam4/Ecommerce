//package com.teamsparta.ecommerce.integrationtest.service
//
//
//
//import com.teamsparta.ecommerce.util.batch.PremiumDealBatchConfiguration
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//import org.springframework.batch.core.Job
//import org.springframework.batch.core.JobExecution
//import org.springframework.batch.test.JobLauncherTestUtils
//import org.springframework.batch.test.context.SpringBatchTest
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
//
//@SpringBatchTest
//@SpringJUnitConfig(PremiumDealBatchConfiguration::class)
//class BatchTest(
//    @Autowired
//    val jobLauncherTestUtils: JobLauncherTestUtils
//) {
//
//    @Test
//    fun testjob(@Autowired job: Job) {
//        jobLauncherTestUtils.job = job
//
//        val jobExecution: JobExecution = jobLauncherTestUtils.launchJob()
//
//        Assertions.assertEquals("COMPLETED", jobExecution.exitStatus.exitCode)
//    }
//}
