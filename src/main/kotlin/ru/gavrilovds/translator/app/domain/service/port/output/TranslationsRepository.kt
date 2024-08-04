package ru.gavrilovds.translator.app.domain.service.port.output

import arrow.core.Either
import ru.gavrilovds.translator.app.domain.core.Translation
import ru.gavrilovds.translator.app.domain.service.port.output.dto.SaveTranslationError

interface TranslationsRepository {

    fun save(translation: Translation): Either<SaveTranslationError, Translation>
}