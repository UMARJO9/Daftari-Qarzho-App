package tj.daftariqarzho.app.core.common.datetime

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object DateFormatter {

    private val shortDate = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val dayMonth = DateTimeFormatter.ofPattern("d MMMM")

    fun formatDate(epochMillis: Long, zone: ZoneId = ZoneId.systemDefault()): String =
        Instant.ofEpochMilli(epochMillis).atZone(zone).format(shortDate)

    fun formatDayMonth(
        epochMillis: Long,
        locale: Locale = Locale.getDefault(),
        zone: ZoneId = ZoneId.systemDefault(),
    ): String =
        Instant.ofEpochMilli(epochMillis).atZone(zone).format(dayMonth.withLocale(locale))

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

    fun startOfDayMillis(epochMillis: Long, zone: ZoneId = ZoneId.systemDefault()): Long =
        Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()
            .atStartOfDay(zone).toInstant().toEpochMilli()

    fun startOfWeekMillis(epochMillis: Long, zone: ZoneId = ZoneId.systemDefault()): Long =
        Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .atStartOfDay(zone).toInstant().toEpochMilli()

    fun startOfMonthMillis(epochMillis: Long, zone: ZoneId = ZoneId.systemDefault()): Long =
        Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()
            .withDayOfMonth(1)
            .atStartOfDay(zone).toInstant().toEpochMilli()

    fun isOverdue(dueDateMillis: Long, nowMillis: Long, zone: ZoneId = ZoneId.systemDefault()): Boolean {
        val due = Instant.ofEpochMilli(dueDateMillis).atZone(zone).toLocalDate()
        val now: LocalDate = Instant.ofEpochMilli(nowMillis).atZone(zone).toLocalDate()
        return due.isBefore(now)
    }
}
