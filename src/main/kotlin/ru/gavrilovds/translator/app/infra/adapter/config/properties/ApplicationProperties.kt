package ru.gavrilovds.translator.app.infra.adapter.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "translator.parallel-execution")
data class ParallelExecutionProperties(
    val threads: Int
)

@ConfigurationProperties(prefix = "translator.google")
data class GoogleProperties(
    val translatorUrl: String
)