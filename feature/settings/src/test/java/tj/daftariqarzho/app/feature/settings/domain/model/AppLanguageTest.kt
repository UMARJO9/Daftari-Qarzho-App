package tj.daftariqarzho.app.feature.settings.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AppLanguageTest {

    @Test
    fun `тег ru даёт русский`() {
        assertEquals(AppLanguage.RUSSIAN, AppLanguage.fromTag("ru"))
    }

    @Test
    fun `тег tg даёт таджикский`() {
        assertEquals(AppLanguage.TAJIK, AppLanguage.fromTag("tg"))
    }

    @Test
    fun `тег с регионом распознаётся по языку`() {
        assertEquals(AppLanguage.RUSSIAN, AppLanguage.fromTag("ru-RU"))
    }

    @Test
    fun `null возвращает таджикский по умолчанию`() {
        assertEquals(AppLanguage.TAJIK, AppLanguage.fromTag(null))
    }

    @Test
    fun `неизвестный тег возвращает таджикский по умолчанию`() {
        assertEquals(AppLanguage.TAJIK, AppLanguage.fromTag("en"))
    }
}
