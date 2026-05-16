package com.virtualworld.agendadeventas.core.locale

import java.util.Locale

enum class AppLanguage(val tag: String, val nativeName: String) {
    ENGLISH("en", "English"),
    SPANISH("es", "Español");

    companion object {
        val supported = entries.toList()

        fun fromTag(tag: String?): AppLanguage? =
            entries.firstOrNull { it.tag == tag }

        fun fromSystem(): AppLanguage =
            if (Locale.getDefault().language == SPANISH.tag) SPANISH else ENGLISH
    }
}
