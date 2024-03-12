package com.teamsparta.ecommerce

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
@EnableBatchProcessing
class EcommerceApplication
fun main(args: Array<String>) {
    runApplication<EcommerceApplication>(*args)
}

