package com.virtualworld.agendadeventas.core.locale

import java.util.Locale

enum class AppLanguage(val tag: String, val nativeName: String) {
    ENGLISH("en", "English"),
    SPANISH("es", "Español"),
    CHINESE("zh", "中文"),
    HINDI("hi", "हिन्दी"),
    ARABIC("ar", "العربية");

    companion object {
        val supported = entries.toList()

        fun fromTag(tag: String?): AppLanguage? =
            entries.firstOrNull { it.tag == tag }

        fun fromSystem(): AppLanguage {
            val systemLang = Locale.getDefault().language
            return entries.firstOrNull { it.tag == systemLang } ?: ENGLISH
        }
    }
}
