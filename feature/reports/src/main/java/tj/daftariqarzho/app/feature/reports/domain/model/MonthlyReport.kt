package tj.daftariqarzho.app.feature.reports.domain.model

data class ReportDebtor(
    val id: Long,
    val name: String,
    val balanceInDirams: Long,
)

data class MonthlyReport(
    val givenInDirams: Long,
    val receivedInDirams: Long,
    val topDebtors: List<ReportDebtor>,
)
