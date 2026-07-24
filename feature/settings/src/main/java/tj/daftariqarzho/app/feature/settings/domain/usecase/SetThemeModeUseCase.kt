package tj.daftariqarzho.app.feature.settings.domain.usecase

import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode
import tj.daftariqarzho.app.feature.settings.domain.repository.ThemeRepository

class SetThemeModeUseCase(
    private val repository: ThemeRepository,
) {
    suspend operator fun invoke(mode: ThemeMode) {
        repository.setThemeMode(mode)
    }
}
