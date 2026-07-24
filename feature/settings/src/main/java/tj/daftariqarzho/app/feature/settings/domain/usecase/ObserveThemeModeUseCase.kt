package tj.daftariqarzho.app.feature.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode
import tj.daftariqarzho.app.feature.settings.domain.repository.ThemeRepository

class ObserveThemeModeUseCase(
    private val repository: ThemeRepository,
) {
    operator fun invoke(): Flow<ThemeMode> = repository.observeThemeMode()
}
