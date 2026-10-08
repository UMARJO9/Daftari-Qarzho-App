package tj.daftariqarzho.app.feature.transactions.presentation

import tj.daftariqarzho.app.feature.transactions.domain.model.DayGroup
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionPeriod

data class TransactionsUiState(
    val isLoading: Boolean = true,
    val period: TransactionPeriod = TransactionPeriod.ALL,
    val groups: List<DayGroup> = emptyList(),
) {
    val isEmpty: Boolean get() = !isLoading && groups.isEmpty()
}
