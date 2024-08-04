package ru.gavrilovds.translator.app.domain.service.port.output.dto

sealed class TranslateByGoogleError {
    data class InternalError(val throwable: Throwable) : TranslateByGoogleError()
}