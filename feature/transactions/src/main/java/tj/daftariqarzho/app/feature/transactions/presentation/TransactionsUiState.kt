package tj.daftariqarzho.app.feature.transactions.presentation

import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionItem

enum class TransactionPeriod {
    ALL,
    TODAY,
    WEEK,
    MONTH,
}

data class DayGroup(
    val dayMillis: Long,
    val items: List<TransactionItem>,
) {
    val netInDirams: Long get() = items.sumOf { it.amountInDirams }
}

data class TransactionsUiState(
    val isLoading: Boolean = true,
    val period: TransactionPeriod = TransactionPeriod.ALL,
    val groups: List<DayGroup> = emptyList(),
) {
    val isEmpty: Boolean get() = !isLoading && groups.isEmpty()
}
