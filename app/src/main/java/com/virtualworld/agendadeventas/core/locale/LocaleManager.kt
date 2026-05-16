package com.virtualworld.agendadeventas.core.locale

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleManager {

    private const val PREFS_NAME = "my_prefs"
    private const val KEY_LANGUAGE = "app_language"

    fun getSelectedLanguage(context: Context): AppLanguage {
        val tag = getSavedLanguageTag(context)
        return AppLanguage.fromTag(tag) ?: AppLanguage.fromSystem()
    }

    fun setLanguage(context: Context, language: AppLanguage) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, language.tag)
            .commit()
        applyLanguage(language)
    }

    fun applyLocale(context: Context): Context {
        val tag = getSavedLanguageTag(context) ?: return context
        val language = AppLanguage.fromTag(tag) ?: return context
        return wrapContext(context, language)
    }

    private fun getSavedLanguageTag(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, null)
            ?.takeIf { it.isNotEmpty() }

    private fun applyLanguage(language: AppLanguage) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(language.tag)
        )
    }

    private fun wrapContext(context: Context, language: AppLanguage): Context {
        val locale = Locale.forLanguageTag(language.tag)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLocales(LocaleList(locale))
        return context.createConfigurationContext(config)
    }
}
