package tj.daftariqarzho.app.core.common.datetime

import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class DayTickerTest {

    private val zone = ZoneId.of("Asia/Dushanbe")

    @Test
    fun `выдаёт начало текущего дня и следующее после полуночи`() = runTest {
        val start = LocalDate.of(2026, 9, 30).atTime(23, 50).atZone(zone).toInstant().toEpochMilli()

        val days = DayTicker.dayStarts(now = { start + testScheduler.currentTime }, zone = { zone })
            .take(2)
            .toList()

        assertEquals(
            listOf(
                LocalDate.of(2026, 9, 30).atStartOfDay(zone).toInstant().toEpochMilli(),
                LocalDate.of(2026, 10, 1).atStartOfDay(zone).toInstant().toEpochMilli(),
            ),
            days,
        )
    }
}
