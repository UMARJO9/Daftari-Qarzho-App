package tj.daftariqarzho.app.feature.reports.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import tj.daftariqarzho.app.core.database.dao.DebtorDao
import tj.daftariqarzho.app.core.database.dao.TransactionDao
import tj.daftariqarzho.app.feature.reports.data.mapper.toTopDebtors
import tj.daftariqarzho.app.feature.reports.domain.model.MonthlyReport
import tj.daftariqarzho.app.feature.reports.domain.repository.ReportsRepository

class ReportsRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val debtorDao: DebtorDao,
) : ReportsRepository {

    override fun observeMonthlyReport(fromMillis: Long): Flow<MonthlyReport> =
        combine(
            transactionDao.observeGivenSince(fromMillis),
            transactionDao.observeReceivedSince(fromMillis),
            debtorDao.observeDebtorsWithBalance(),
        ) { given, received, debtors ->
            MonthlyReport(
                givenInDirams = given,
                receivedInDirams = received,
                topDebtors = debtors.toTopDebtors(),
            )
        }
}
