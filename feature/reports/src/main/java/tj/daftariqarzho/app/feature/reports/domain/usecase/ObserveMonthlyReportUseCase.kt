package tj.daftariqarzho.app.feature.reports.domain.usecase

import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.feature.reports.domain.model.MonthlyReport
import tj.daftariqarzho.app.feature.reports.domain.repository.ReportsRepository

class ObserveMonthlyReportUseCase(
    private val repository: ReportsRepository,
) {
    operator fun invoke(fromMillis: Long): Flow<MonthlyReport> =
        repository.observeMonthlyReport(fromMillis)
}
