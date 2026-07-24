package tj.daftariqarzho.app.feature.debtors.domain.model

data class DebtTransaction(
    val id: Long,
    val debtorId: Long,
    val amountInDirams: Long,
    val comment: String?,
    val dueDate: Long?,
    val photoUri: String?,
    val createdAt: Long,
)
