package tj.daftariqarzho.app.core.common.datetime

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.ZoneId

object DayTicker {

    private const val BOUNDARY_MARGIN_MILLIS = 1_000L

    fun dayStarts(
        now: () -> Long = System::currentTimeMillis,
        zone: () -> ZoneId = ZoneId::systemDefault,
    ): Flow<Long> = flow {
        while (true) {
            val nowMillis = now()
            val currentZone = zone()
            emit(DateFormatter.startOfDayMillis(nowMillis, currentZone))
            val nextDay = DateFormatter.startOfNextDayMillis(nowMillis, currentZone)
            delay(nextDay - nowMillis + BOUNDARY_MARGIN_MILLIS)
        }
    }
}
