package tj.daftariqarzho.app.feature.reports.presentation

import tj.daftariqarzho.app.feature.reports.domain.model.ReportDebtor

data class ReportsUiState(
    val isLoading: Boolean = true,
    val monthStartMillis: Long = 0,
    val givenInDirams: Long = 0,
    val receivedInDirams: Long = 0,
    val topDebtors: List<ReportDebtor> = emptyList(),
) {
    val hasDebtors: Boolean get() = topDebtors.isNotEmpty()
}
