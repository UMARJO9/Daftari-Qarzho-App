package tj.daftariqarzho.app.feature.transactions.domain.model

data class DayGroup(
    val dayMillis: Long,
    val items: List<TransactionItem>,
) {
    val netInDirams: Long get() = items.sumOf { it.amountInDirams }
}
