package ru.gavrilovds.translator.app.infra.adapter.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import ru.gavrilovds.translator.app.infra.adapter.config.properties.GoogleProperties
import ru.gavrilovds.translator.app.infra.adapter.config.properties.ParallelExecutionProperties

@Configuration
@EnableConfigurationProperties(ParallelExecutionProperties::class, GoogleProperties::class)
class ApplicationConfig