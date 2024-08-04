package ru.gavrilovds.translator.app.domain.service.port.input

import arrow.core.Either
import ru.gavrilovds.translator.app.domain.core.Translation
import ru.gavrilovds.translator.app.domain.service.port.input.dto.TranslateByGoogleRequest
import ru.gavrilovds.translator.app.domain.service.port.input.dto.TranslateError

interface TranslateByGoogleUseCase {

    fun execute(request: TranslateByGoogleRequest): Either<TranslateError, Translation>
}