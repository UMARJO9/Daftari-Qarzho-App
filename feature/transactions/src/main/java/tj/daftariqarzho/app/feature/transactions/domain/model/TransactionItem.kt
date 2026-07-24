package tj.daftariqarzho.app.feature.transactions.domain.model

data class TransactionItem(
    val id: Long,
    val debtorId: Long,
    val debtorName: String,
    val amountInDirams: Long,
    val comment: String?,
    val createdAt: Long,
)
