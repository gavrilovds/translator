package ru.gavrilovds.translator.app.domain.service.port.output.dto

sealed class SaveTranslationError {
    data class InternalError(val throwable: Throwable) : SaveTranslationError()
}