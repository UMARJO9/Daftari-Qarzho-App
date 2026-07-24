package tj.daftariqarzho.app.feature.reports.domain.repository

import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.feature.reports.domain.model.MonthlyReport

interface ReportsRepository {
    fun observeMonthlyReport(fromMillis: Long): Flow<MonthlyReport>
}
