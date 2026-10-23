package tj.daftariqarzho.app.feature.settings.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import tj.daftariqarzho.app.feature.settings.FakeNightModeController
import tj.daftariqarzho.app.feature.settings.FakeThemeRepository
import tj.daftariqarzho.app.feature.settings.domain.model.ThemeMode

class ApplySavedThemeModeUseCaseTest {

    @Test
    fun `сохранённая тема применяется при старте`() = runTest {
        val controller = FakeNightModeController()

        ApplySavedThemeModeUseCase(FakeThemeRepository(ThemeMode.DARK), controller)()

        assertEquals(listOf(ThemeMode.DARK), controller.applied)
    }
}
