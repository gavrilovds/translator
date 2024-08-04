package ru.gavrilovds.translator.app.domain.service.port

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.either
import org.springframework.stereotype.Service
import ru.gavrilovds.translator.app.domain.core.Translation
import ru.gavrilovds.translator.app.domain.core.Word
import ru.gavrilovds.translator.app.domain.service.port.input.TranslateByGoogleUseCase
import ru.gavrilovds.translator.app.domain.service.port.input.dto.TranslateByGoogleRequest
import ru.gavrilovds.translator.app.domain.service.port.input.dto.TranslateError
import ru.gavrilovds.translator.app.domain.service.port.output.GoogleTranslatorClient
import ru.gavrilovds.translator.app.domain.service.port.output.TranslationsRepository
import ru.gavrilovds.translator.app.domain.service.port.output.dto.SaveTranslationError
import ru.gavrilovds.translator.app.domain.service.port.output.dto.TranslateByGoogleError
import java.util.concurrent.ExecutorService

@Service
class TranslateByGoogleService(
    private val googleTranslatorClient: GoogleTranslatorClient,
    private val translationsRepository: TranslationsRepository,
    private val googleExecutorService: ExecutorService
) : TranslateByGoogleUseCase {

    override fun execute(request: TranslateByGoogleRequest): Either<TranslateError, Translation> {
        return request.words
            .map { word ->
                googleExecutorService.submit<Either<TranslateByGoogleError, Word>> {
                    googleTranslatorClient.translate(
                        word = word,
                        from = request.from,
                        to = request.to
                    )
                }
            }
            .map { future ->
                future.get()
                    .mapLeft { error -> mapError(error) }
            }
            .let { either { it.bindAll() } }
            .flatMap { words ->
                translationsRepository.save(
                    Translation(
                        ip = request.ip,
                        beforeTranslation = request.words,
                        afterTranslation = words
                    )
                )
                    .mapLeft { error -> mapError(error) }
            }
    }

    private fun mapError(error: TranslateByGoogleError): TranslateError {
        return when (error) {
            is TranslateByGoogleError.InternalError -> TranslateError.InternalError(error.throwable)
        }
    }

    private fun mapError(error: SaveTranslationError): TranslateError {
        return when (error) {
            is SaveTranslationError.InternalError -> TranslateError.InternalError(error.throwable)
        }
    }
}