package com.example.payroll

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoadDatabase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun initDatabase(employeeRepository: EmployeeRepository) = CommandLineRunner {
        logger.info("Preloading " + employeeRepository.save(Employee(name = "Olaf Hong", role = "manager")))
        logger.info("Preloading " + employeeRepository.save(Employee(name = "gaonasi", role = "player")))
    }
}
