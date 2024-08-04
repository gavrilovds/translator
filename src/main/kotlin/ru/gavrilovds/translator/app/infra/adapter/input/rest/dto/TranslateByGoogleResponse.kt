package ru.gavrilovds.translator.app.infra.adapter.input.rest.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ответ на запрос перевода слов при помощи google")
data class TranslateByGoogleResponse(

    @Schema(description = "Слова до перевода")
    val beforeTranslation: String,

    @Schema(description = "Слова после перевода")
    val afterTranslation: String
)
