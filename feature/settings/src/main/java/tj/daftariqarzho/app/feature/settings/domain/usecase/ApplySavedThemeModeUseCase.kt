package tj.daftariqarzho.app.feature.settings.domain.usecase

import kotlinx.coroutines.flow.first
import tj.daftariqarzho.app.feature.settings.domain.NightModeController
import tj.daftariqarzho.app.feature.settings.domain.repository.ThemeRepository

class ApplySavedThemeModeUseCase(
    private val repository: ThemeRepository,
    private val nightModeController: NightModeController,
) {
    suspend operator fun invoke() {
        nightModeController.apply(repository.observeThemeMode().first())
    }
}
