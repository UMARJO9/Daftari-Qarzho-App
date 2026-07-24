package tj.daftariqarzho.app.feature.settings.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import tj.daftariqarzho.app.feature.settings.FakeLocaleManager
import tj.daftariqarzho.app.feature.settings.FakeThemeRepository
import tj.daftariqarzho.app.feature.settings.MainDispatcherRule
import tj.daftariqarzho.app.feature.settings.domain.model.AppLanguage
import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode
import tj.daftariqarzho.app.feature.settings.domain.usecase.ObserveThemeModeUseCase
import tj.daftariqarzho.app.feature.settings.domain.usecase.SetThemeModeUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun viewModel(
        themeRepository: FakeThemeRepository,
        localeManager: FakeLocaleManager,
    ) = SettingsViewModel(
        observeThemeMode = ObserveThemeModeUseCase(themeRepository),
        setThemeMode = SetThemeModeUseCase(themeRepository),
        localeManager = localeManager,
    )

    @Test
    fun `начальное состояние берёт язык из менеджера`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = viewModel(FakeThemeRepository(), FakeLocaleManager(AppLanguage.RUSSIAN))
        assertEquals(AppLanguage.RUSSIAN, vm.uiState.value.language)
    }

    @Test
    fun `выбор темы сохраняется в репозитории и отражается в состоянии`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val themeRepository = FakeThemeRepository(ThemeMode.SYSTEM)
            val vm = viewModel(themeRepository, FakeLocaleManager())
            backgroundScope.launch { vm.uiState.collect {} }
            advanceUntilIdle()

            vm.onEvent(SettingsEvent.ThemeSelected(ThemeMode.DARK))
            advanceUntilIdle()

            assertEquals(ThemeMode.DARK, themeRepository.mode.value)
            assertEquals(ThemeMode.DARK, vm.uiState.value.themeMode)
        }

    @Test
    fun `выбор языка применяется через менеджер и обновляет состояние`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val localeManager = FakeLocaleManager(AppLanguage.TAJIK)
            val vm = viewModel(FakeThemeRepository(), localeManager)
            backgroundScope.launch { vm.uiState.collect {} }
            advanceUntilIdle()

            vm.onEvent(SettingsEvent.LanguageSelected(AppLanguage.RUSSIAN))
            advanceUntilIdle()

            assertEquals(AppLanguage.RUSSIAN, localeManager.current)
            assertEquals(AppLanguage.RUSSIAN, vm.uiState.value.language)
        }
}
