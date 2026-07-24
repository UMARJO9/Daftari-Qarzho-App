package tj.daftariqarzho.app.feature.settings.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tj.daftariqarzho.app.feature.settings.data.LocaleManagerImpl
import tj.daftariqarzho.app.feature.settings.data.ThemeRepositoryImpl
import tj.daftariqarzho.app.feature.settings.domain.LocaleManager
import tj.daftariqarzho.app.feature.settings.domain.repository.ThemeRepository
import tj.daftariqarzho.app.feature.settings.domain.usecase.ObserveThemeModeUseCase
import tj.daftariqarzho.app.feature.settings.domain.usecase.SetThemeModeUseCase
import tj.daftariqarzho.app.feature.settings.presentation.SettingsViewModel

private const val SETTINGS_DATASTORE = "settings"

val settingsModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile(SETTINGS_DATASTORE)
        }
    }
    single<ThemeRepository> { ThemeRepositoryImpl(get()) }
    single<LocaleManager> { LocaleManagerImpl() }
    factory { ObserveThemeModeUseCase(get()) }
    factory { SetThemeModeUseCase(get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
}
