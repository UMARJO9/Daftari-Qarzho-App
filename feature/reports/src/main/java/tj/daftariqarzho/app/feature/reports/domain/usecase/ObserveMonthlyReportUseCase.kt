package tj.daftariqarzho.app.feature.reports.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.core.common.datetime.DayTicker
import tj.daftariqarzho.app.feature.reports.domain.model.MonthlyReport
import tj.daftariqarzho.app.feature.reports.domain.repository.ReportsRepository

class ObserveMonthlyReportUseCase(
    private val repository: ReportsRepository,
    private val dayStarts: Flow<Long> = DayTicker.dayStarts(),
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<MonthlyReport> =
        dayStarts
            .map { DateFormatter.startOfMonthMillis(it) }
            .distinctUntilChanged()
            .flatMapLatest { monthStart ->
                repository.observeMonthlyReport(monthStart, DateFormatter.startOfNextMonthMillis(monthStart))
            }
}
