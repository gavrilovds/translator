package ru.gavrilovds.translator.app.domain.core

data class Translation(
    val ip: IpAddress,
    val beforeTranslation: List<Word>,
    val afterTranslation: List<Word>
) {

    fun beforeTranslationAsString() = beforeTranslation.joinToString(",") { it.value }
    fun afterTranslationAsString() = afterTranslation.joinToString(",") { it.value }
}
