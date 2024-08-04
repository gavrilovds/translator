package ru.gavrilovds.translator.app.infra.adapter.input.rest

import arrow.core.Either
import arrow.core.flatMap
import org.springframework.stereotype.Component
import ru.gavrilovds.translator.app.domain.core.Translation
import ru.gavrilovds.translator.app.domain.service.port.input.TranslateByGoogleUseCase
import ru.gavrilovds.translator.app.domain.service.port.input.dto.TranslateByGoogleRequest
import ru.gavrilovds.translator.app.domain.service.port.input.dto.TranslateError
import ru.gavrilovds.translator.app.infra.adapter.input.rest.dto.TranslateByGoogleError
import ru.gavrilovds.translator.app.infra.adapter.input.rest.dto.TranslateByGoogleResponse

@Component
class TranslateByGoogleV1Usecase(
    private val translateByGoogleUseCase: TranslateByGoogleUseCase
) {

    fun execute(
        ip: String?,
        words: String,
        from: String,
        to: String
    ): Either<TranslateByGoogleError, TranslateByGoogleResponse> {
        return TranslateByGoogleRequest.create(
            ip = ip,
            words = words,
            from = from,
            to = to
        )
            .mapLeft { TranslateError.ValidationErrors(it) }
            .flatMap { request -> translateByGoogleUseCase.execute(request) }
            .mapLeft { error -> mapError(error) }
            .map { success -> mapSuccess(success) }
    }

    private fun mapSuccess(success: Translation) = TranslateByGoogleResponse(
        beforeTranslation = success.beforeTranslationAsString(),
        afterTranslation = success.afterTranslationAsString()
    )

    private fun mapError(error: TranslateError): TranslateByGoogleError {
        return when (error) {
            is TranslateError.ValidationErrors -> TranslateByGoogleError(
                code = TranslateByGoogleError.ErrorCode.VALIDATION_ERROR,
                message = error.errors.joinToString(",")
            )

            is TranslateError.InternalError -> throw RuntimeException(error.throwable)
        }
    }
}