package tj.daftariqarzho.app.feature.settings.domain.repository

import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode

interface ThemeRepository {
    fun observeThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
}
