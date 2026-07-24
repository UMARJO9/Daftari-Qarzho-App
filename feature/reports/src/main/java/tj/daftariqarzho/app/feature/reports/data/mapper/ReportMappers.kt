package tj.daftariqarzho.app.feature.reports.data.mapper

import tj.daftariqarzho.app.core.database.entity.DebtorWithBalance
import tj.daftariqarzho.app.feature.reports.domain.model.ReportDebtor

const val TOP_DEBTORS_LIMIT = 5

fun List<DebtorWithBalance>.toTopDebtors(limit: Int = TOP_DEBTORS_LIMIT): List<ReportDebtor> =
    filter { it.balance < 0 }
        .sortedBy { it.balance }
        .take(limit)
        .map { ReportDebtor(it.debtor.id, it.debtor.name, it.balance) }
