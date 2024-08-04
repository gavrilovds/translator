package ru.gavrilovds.translator.app.infra.adapter.input.rest

object HttpPaths {
    const val BASE_PATH = "/translator"
    const val VERSION_1 = "/v1"
    const val TRANSLATE_BY_GOOGLE_V1_PATH = "$BASE_PATH$VERSION_1/translate-by-google"
}