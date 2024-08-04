package ru.gavrilovds.translator.app.infra.adapter.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.gavrilovds.translator.app.infra.adapter.config.properties.ParallelExecutionProperties
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
@Import(ApplicationConfig::class)
class ParallelExecutionConfig(
    private val parallelExecutionProperties: ParallelExecutionProperties
) {

    @Bean
    fun googleExecutorService(): ExecutorService =
        Executors.newFixedThreadPool(parallelExecutionProperties.threads)
}
