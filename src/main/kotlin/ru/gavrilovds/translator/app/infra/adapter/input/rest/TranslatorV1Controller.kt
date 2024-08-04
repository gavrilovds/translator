package ru.gavrilovds.translator.app.infra.adapter.input.rest

import arrow.core.Either
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.gavrilovds.translator.app.infra.adapter.input.rest.HttpPaths.TRANSLATE_BY_GOOGLE_V1_PATH
import ru.gavrilovds.translator.app.infra.adapter.input.rest.dto.TranslateByGoogleError
import ru.gavrilovds.translator.app.infra.adapter.input.rest.dto.TranslateByGoogleResponse

@Tag(name = "Translator API V1", description = "API для перевода слов")
@RestController
class TranslatorV1Controller(
    private val translateByGoogleV1Usecase: TranslateByGoogleV1Usecase
) {

    @Operation(
        summary = "Перевод набора слов при помощи google",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Успешный запрос",
                content = [Content(schema = Schema(implementation = TranslateByGoogleResponse::class))]
            ),
            ApiResponse(
                responseCode = "422",
                description = "Ошибка при переводе набора слов при помощи google",
                content = [Content(schema = Schema(implementation = TranslateByGoogleError::class))]
            )
        ]
    )
    @PostMapping(TRANSLATE_BY_GOOGLE_V1_PATH)
    fun translateByGoogle(
        @RequestParam words: String,
        @RequestParam from: String,
        @RequestParam to: String,
        request: HttpServletRequest
    ): ResponseEntity<*> {
        return translateByGoogleV1Usecase.execute(
            ip = request.remoteAddr,
            words = words,
            from = from,
            to = to
        ).toResponseEntity()
    }

    private fun Either<*, *>.toResponseEntity(): ResponseEntity<*> {
        return fold(
            { error -> ResponseEntity.unprocessableEntity().body(error) },
            { success -> ResponseEntity.ok().body(success) }
        )
    }
}