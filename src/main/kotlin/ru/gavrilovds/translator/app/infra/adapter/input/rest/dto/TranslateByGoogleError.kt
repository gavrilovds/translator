package ru.gavrilovds.translator.app.infra.adapter.input.rest.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ошибка при переводе при помощи google")
data class TranslateByGoogleError(
    val code: ErrorCode,
    @Schema(description = "Описание ошибки")
    val message: String?
) {

    @Schema(description = "Код ошибки")
    enum class ErrorCode {

        @Schema(description = "Ошибка валидации")
        VALIDATION_ERROR
    }
}
