package tj.daftariqarzho.app.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tj.daftariqarzho.app.feature.settings.domain.LocaleManager
import tj.daftariqarzho.app.feature.settings.domain.usecase.ObserveThemeModeUseCase
import tj.daftariqarzho.app.feature.settings.domain.usecase.SetThemeModeUseCase

class SettingsViewModel(
    observeThemeMode: ObserveThemeModeUseCase,
    private val setThemeMode: SetThemeModeUseCase,
    private val localeManager: LocaleManager,
) : ViewModel() {

    private val language = MutableStateFlow(localeManager.currentLanguage())

    val uiState: StateFlow<SettingsUiState> = combine(
        observeThemeMode(),
        language,
    ) { themeMode, currentLanguage ->
        SettingsUiState(themeMode = themeMode, language = currentLanguage)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState(language = localeManager.currentLanguage()),
    )

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ThemeSelected -> viewModelScope.launch {
                setThemeMode(event.mode)
            }

            is SettingsEvent.LanguageSelected -> {
                language.value = event.language
                localeManager.setLanguage(event.language)
            }
        }
    }
}
