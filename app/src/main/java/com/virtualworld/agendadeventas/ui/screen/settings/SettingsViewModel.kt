package com.virtualworld.agendadeventas.ui.screen.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import com.virtualworld.agendadeventas.core.locale.AppLanguage
import com.virtualworld.agendadeventas.core.locale.LocaleManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow(LocaleManager.getSelectedLanguage(context))
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    val availableLanguages: List<AppLanguage> = AppLanguage.supported

    fun selectLanguage(language: AppLanguage) {
        if (language == _selectedLanguage.value) return
        LocaleManager.setLanguage(context, language)
        _selectedLanguage.value = language
    }
}
