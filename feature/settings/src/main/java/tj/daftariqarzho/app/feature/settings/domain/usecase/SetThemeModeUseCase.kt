package tj.daftariqarzho.app.feature.settings.domain.usecase

import tj.daftariqarzho.app.feature.settings.domain.NightModeController
import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode
import tj.daftariqarzho.app.feature.settings.domain.repository.ThemeRepository

class SetThemeModeUseCase(
    private val repository: ThemeRepository,
    private val nightModeController: NightModeController,
) {
    suspend operator fun invoke(mode: ThemeMode) {
        repository.setThemeMode(mode)
        nightModeController.apply(mode)
    }
}
