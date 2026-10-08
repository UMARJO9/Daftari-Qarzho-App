package tj.daftariqarzho.app.core.common.datetime

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

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

    @Test
    fun `startOfWeekMillis возвращает понедельник недели`() {
        val thursday = 7L * day
        assertEquals(4L * day, DateFormatter.startOfWeekMillis(thursday, utc))
    }

    @Test
    fun `startOfWeekMillis в понедельник возвращает тот же день`() {
        val monday = 4L * day
        assertEquals(monday, DateFormatter.startOfWeekMillis(monday + 3_600_000L, utc))
    }

    @Test
    fun `startOfMonthMillis возвращает первое число месяца`() {
        assertEquals(0L, DateFormatter.startOfMonthMillis(19L * day, utc))
    }

    @Test
    fun `дата из DatePicker в UTC переводится в начало локального дня`() {
        val dushanbe = ZoneId.of("Asia/Dushanbe")
        val date = LocalDate.of(2026, 3, 10)
        val pickerMillis = date.atStartOfDay(utc).toInstant().toEpochMilli()
        val local = DateFormatter.utcDateToLocalStartOfDay(pickerMillis, dushanbe)
        assertEquals(date.atStartOfDay(dushanbe).toInstant().toEpochMilli(), local)
        assertEquals(pickerMillis, DateFormatter.localDateToUtcStartOfDay(local, dushanbe))
    }

    @Test
    fun `при отрицательном смещении зоны дата не съезжает на день назад`() {
        val newYork = ZoneId.of("America/New_York")
        val local = DateFormatter.utcDateToLocalStartOfDay(10L * day, newYork)
        assertEquals(DateFormatter.formatDate(10L * day, utc), DateFormatter.formatDate(local, newYork))
    }
}
