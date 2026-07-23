package tj.daftariqarzho.app.core.common.datetime

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * Форматтер дат для приложения.
 *
 * Даты хранятся как epoch-миллисекунды в [Long]. Форматирование выполняется
 * через `java.time` (на API < 26 работает благодаря core library desugaring).
 */
object DateFormatter {

    private val shortDate = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val dayMonth = DateTimeFormatter.ofPattern("d MMMM")

    /** «23.07.2026» из epoch-миллисекунд. */
    fun formatDate(epochMillis: Long, zone: ZoneId = ZoneId.systemDefault()): String =
        Instant.ofEpochMilli(epochMillis).atZone(zone).format(shortDate)

    /** «23 июля» — для заголовков групп в ленте операций. */
    fun formatDayMonth(
        epochMillis: Long,
        locale: Locale = Locale.getDefault(),
        zone: ZoneId = ZoneId.systemDefault(),
    ): String =
        Instant.ofEpochMilli(epochMillis).atZone(zone).format(dayMonth.withLocale(locale))

    /**
     * Количество полных дней просрочки на момент [nowMillis].
     *
     * @return число дней > 0, если срок [dueDateMillis] уже прошёл; иначе 0.
     */
    fun daysOverdue(
        dueDateMillis: Long,
        nowMillis: Long,
        zone: ZoneId = ZoneId.systemDefault(),
    ): Long {
        val due = Instant.ofEpochMilli(dueDateMillis).atZone(zone).toLocalDate()
        val now = Instant.ofEpochMilli(nowMillis).atZone(zone).toLocalDate()
        val days = ChronoUnit.DAYS.between(due, now)
        return if (days > 0) days else 0
    }

    /** Начало дня (00:00 локальной зоны) в epoch-миллисекундах — ключ группировки по дням. */
    fun startOfDayMillis(epochMillis: Long, zone: ZoneId = ZoneId.systemDefault()): Long =
        Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()
            .atStartOfDay(zone).toInstant().toEpochMilli()

    /** Признак того, что [dueDateMillis] строго раньше [nowMillis] (просрочка по дате). */
    fun isOverdue(dueDateMillis: Long, nowMillis: Long, zone: ZoneId = ZoneId.systemDefault()): Boolean {
        val due = Instant.ofEpochMilli(dueDateMillis).atZone(zone).toLocalDate()
        val now: LocalDate = Instant.ofEpochMilli(nowMillis).atZone(zone).toLocalDate()
        return due.isBefore(now)
    }
}
