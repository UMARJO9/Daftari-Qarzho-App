package tj.daftariqarzho.app.feature.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode
import tj.daftariqarzho.app.feature.settings.domain.repository.ThemeRepository

class ThemeRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : ThemeRepository {

    override fun observeThemeMode(): Flow<ThemeMode> =
        dataStore.data.map { preferences ->
            preferences[THEME_KEY]
                ?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() }
                ?: ThemeMode.SYSTEM
        }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = mode.name
        }
    }

    private companion object {
        val THEME_KEY = stringPreferencesKey("theme_mode")
    }
}
