package tj.daftariqarzho.app.core.common.datetime

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.ZoneId

/** Тесты работы с датами: форматирование, просрочка, группировка по дням. */
class DateFormatterTest {

    private val utc = ZoneId.of("UTC")
    private val day = 86_400_000L

    @Test
    fun `formatDate форматирует epoch в dd_MM_yyyy`() {
        assertEquals("01.01.1970", DateFormatter.formatDate(0L, utc))
    }

    @Test
    fun `daysOverdue считает полные дни просрочки`() {
        val due = 100L * day
        assertEquals(5L, DateFormatter.daysOverdue(due, due + 5 * day, utc))
    }

    @Test
    fun `daysOverdue равен нулю если срок в будущем`() {
        val now = 100L * day
        assertEquals(0L, DateFormatter.daysOverdue(now + 3 * day, now, utc))
    }

    @Test
    fun `isOverdue истинно когда срок раньше сегодня`() {
        val due = 100L * day
        assertTrue(DateFormatter.isOverdue(due, due + day, utc))
    }

    @Test
    fun `isOverdue ложно в тот же день`() {
        val due = 100L * day
        assertFalse(DateFormatter.isOverdue(due, due, utc))
    }

    @Test
    fun `startOfDayMillis отбрасывает время внутри суток`() {
        assertEquals(0L, DateFormatter.startOfDayMillis(3_600_000L, utc))
    }
}
