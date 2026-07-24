package tj.daftariqarzho.app.feature.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import tj.daftariqarzho.app.feature.settings.domain.LocaleManager
import tj.daftariqarzho.app.feature.settings.domain.model.AppLanguage
import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode
import tj.daftariqarzho.app.feature.settings.domain.repository.ThemeRepository

class FakeThemeRepository(initial: ThemeMode = ThemeMode.SYSTEM) : ThemeRepository {
    val mode = MutableStateFlow(initial)
    override fun observeThemeMode(): Flow<ThemeMode> = mode
    override suspend fun setThemeMode(mode: ThemeMode) {
        this.mode.value = mode
    }
}

class FakeLocaleManager(initial: AppLanguage = AppLanguage.TAJIK) : LocaleManager {
    var current: AppLanguage = initial
    override fun currentLanguage(): AppLanguage = current
    override fun setLanguage(language: AppLanguage) {
        current = language
    }
}
