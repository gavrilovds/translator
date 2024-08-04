package ru.gavrilovds.translator.app.domain.service.port.input.dto

import arrow.core.NonEmptyList
import ru.gavrilovds.translator.app.domain.validation.ValidationError

sealed class TranslateError {
    data class ValidationErrors(val errors: NonEmptyList<ValidationError>) : TranslateError()
    data class InternalError(val throwable: Throwable) : TranslateError()
}