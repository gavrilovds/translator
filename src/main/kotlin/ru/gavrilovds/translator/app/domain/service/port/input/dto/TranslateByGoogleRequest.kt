package ru.gavrilovds.translator.app.domain.service.port.input.dto

import arrow.core.EitherNel
import arrow.core.raise.either
import ru.gavrilovds.translator.app.domain.core.IpAddress
import ru.gavrilovds.translator.app.domain.core.Language
import ru.gavrilovds.translator.app.domain.core.Word
import ru.gavrilovds.translator.app.domain.validation.ValidationError

data class TranslateByGoogleRequest(
    val ip: IpAddress,
    val words: List<Word>,
    val from: Language,
    val to: Language
) {

    companion object {

        fun create(
            ip: String?,
            words: String,
            from: String,
            to: String
        ): EitherNel<ValidationError, TranslateByGoogleRequest> {
            return either {
                TranslateByGoogleRequest(
                    ip = IpAddress.create(ip).bind(),
                    words = words.split(",").map { Word(it) },
                    from = Language.create(from).bind(),
                    to = Language.create(to).bind()
                )
            }
        }
    }
}