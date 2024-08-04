package ru.gavrilovds.translator.app.domain.service.port.output

import arrow.core.Either
import ru.gavrilovds.translator.app.domain.core.Language
import ru.gavrilovds.translator.app.domain.core.Word
import ru.gavrilovds.translator.app.domain.service.port.output.dto.TranslateByGoogleError

interface GoogleTranslatorClient {

    fun translate(word: Word, from: Language, to: Language): Either<TranslateByGoogleError, Word>
}