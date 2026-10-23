package tj.daftariqarzho.app.feature.debtors.presentation

import tj.daftariqarzho.app.feature.debtors.domain.model.DebtTransaction
import tj.daftariqarzho.app.feature.debtors.domain.model.Debtor

data class DebtorDetailUiState(
    val isLoading: Boolean = true,
    val notFound: Boolean = false,
    val debtor: Debtor? = null,
    val balanceInDirams: Long = 0,
    val transactions: List<DebtTransaction> = emptyList(),
) {
    val isEmpty: Boolean get() = !isLoading && transactions.isEmpty()
}
