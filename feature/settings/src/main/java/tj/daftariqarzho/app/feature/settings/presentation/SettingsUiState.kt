package tj.daftariqarzho.app.feature.settings.presentation

import tj.daftariqarzho.app.feature.settings.domain.model.AppLanguage
import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: AppLanguage = AppLanguage.TAJIK,
)
