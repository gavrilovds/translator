package ru.gavrilovds.translator.app.infra.adapter.output.rest

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.client.getForObject
import org.springframework.web.util.UriComponentsBuilder
import ru.gavrilovds.translator.app.domain.core.Language
import ru.gavrilovds.translator.app.domain.core.Word
import ru.gavrilovds.translator.app.domain.service.port.output.GoogleTranslatorClient
import ru.gavrilovds.translator.app.domain.service.port.output.dto.TranslateByGoogleError
import ru.gavrilovds.translator.app.infra.adapter.config.properties.GoogleProperties

@Component
class GoogleTranslatorHttpClient(
    private val restTemplate: RestTemplate,
    private val googleProperties: GoogleProperties,
    private val objectMapper: ObjectMapper
) : GoogleTranslatorClient {

    companion object {
        private val logger = LoggerFactory.getLogger(GoogleTranslatorHttpClient::class.java)
        private const val CLIENT_PARAM = "client"
        private const val CLIENT_PARAM_VALUE = "gtx"
        private const val DT_PARAM = "dt"
        private const val DT_PARAM_VALUE = "t"
        private const val SOURCE_LANGUAGE_PARAM = "sl"
        private const val TARGET_LANGUAGE_PARAM = "tl"
        private const val QUERY_PARAM = "q"
    }

    override fun translate(
        word: Word,
        from: Language,
        to: Language
    ): Either<TranslateByGoogleError, Word> {
        return Either.catch {

            val uri = UriComponentsBuilder.fromHttpUrl(googleProperties.translatorUrl)
                .queryParam(CLIENT_PARAM, CLIENT_PARAM_VALUE)
                .queryParam(DT_PARAM, DT_PARAM_VALUE)
                .queryParam(SOURCE_LANGUAGE_PARAM, from.code)
                .queryParam(TARGET_LANGUAGE_PARAM, to.code)
                .queryParam(QUERY_PARAM, word.value)
                .toUriString()

            val response = restTemplate.getForEntity(uri, String::class.java)

            val translatedWord = objectMapper.readTree(response.body).get(0).get(0).get(0).asText()

            logger.info("Translated word: {}", translatedWord)

            Word(translatedWord)
        }
            .mapLeft { TranslateByGoogleError.InternalError(it) }
    }
}