package ru.gavrilovds.translator.app.domain.core

import arrow.core.EitherNel
import arrow.core.leftNel
import arrow.core.right
import ru.gavrilovds.translator.app.domain.validation.ValidationError
import java.util.regex.Pattern

@JvmInline
value class IpAddress(val value: String) {
    companion object {

        private val IP_ADDRESS = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))"
        )

        fun create(value: String?): EitherNel<ValidationError, IpAddress> {
            if (value.isNullOrBlank() || !IP_ADDRESS.matcher(value).matches()) {
                return ValidationError("Invalid value for IpAddress = $value").leftNel()
            }
            return IpAddress(value).right()
        }
    }
}