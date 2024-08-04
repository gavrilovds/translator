package ru.gavrilovds.translator.app.infra.adapter.output.persistence.jdbc

import arrow.core.Either
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import ru.gavrilovds.translator.app.domain.core.IpAddress
import ru.gavrilovds.translator.app.domain.core.Translation
import ru.gavrilovds.translator.app.domain.core.Word
import ru.gavrilovds.translator.app.domain.service.port.output.TranslationsRepository
import ru.gavrilovds.translator.app.domain.service.port.output.dto.SaveTranslationError
import java.sql.ResultSet

@Repository
class TranslationsJdbcRepository(
    private val jdbcClient: JdbcClient
) : TranslationsRepository {

    companion object {
        private const val TABLE_NAME = "translation"

        private const val USER_IP_COLUMN = "user_ip"
        private const val BEFORE_TRANSLATION_COLUMN = "before_translation"
        private const val AFTER_TRANSLATION_COLUMN = "after_translation"

        private const val SAVE_TRANSLATION = """
            INSERT INTO $TABLE_NAME ($USER_IP_COLUMN, $BEFORE_TRANSLATION_COLUMN, $AFTER_TRANSLATION_COLUMN) VALUES(:$USER_IP_COLUMN, :$BEFORE_TRANSLATION_COLUMN, :$AFTER_TRANSLATION_COLUMN)
            ON CONFLICT 
                DO NOTHING 
            RETURNING *
        """

        private val MAPPER = RowMapper { rs: ResultSet, _: Int ->
            Translation(
                ip = IpAddress(rs.getString(USER_IP_COLUMN)),
                beforeTranslation = rs.getString(BEFORE_TRANSLATION_COLUMN).split(",")
                    .map { Word(it) },
                afterTranslation = rs.getString(AFTER_TRANSLATION_COLUMN).split(",")
                    .map { Word(it) }
            )
        }
    }

    override fun save(translation: Translation): Either<SaveTranslationError, Translation> {
        return Either.catch {
            jdbcClient.sql(SAVE_TRANSLATION)
                .param(USER_IP_COLUMN, translation.ip.value)
                .param(BEFORE_TRANSLATION_COLUMN, translation.beforeTranslationAsString())
                .param(AFTER_TRANSLATION_COLUMN, translation.afterTranslationAsString())
                .query(MAPPER)
                .single()
        }
            .mapLeft { SaveTranslationError.InternalError(it) }
    }
}