package tj.daftariqarzho.app.feature.debtors.presentation

import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorSummary

data class DebtorsUiState(
    val isLoading: Boolean = true,
    val query: String = "",
    val debtors: List<DebtorSummary> = emptyList(),
    val totalBalanceInDirams: Long = 0,
) {
    val isEmpty: Boolean get() = !isLoading && debtors.isEmpty()
}
