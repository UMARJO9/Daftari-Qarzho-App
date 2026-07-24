package tj.daftariqarzho.app.feature.settings.presentation

import tj.daftariqarzho.app.feature.settings.domain.model.AppLanguage
import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode

sealed interface SettingsEvent {

    data class ThemeSelected(val mode: ThemeMode) : SettingsEvent

    data class LanguageSelected(val language: AppLanguage) : SettingsEvent
}
