package tj.daftariqarzho.app.feature.debtors.domain.model

data class DebtorSummary(
    val debtor: Debtor,
    val balanceInDirams: Long,
    val isOverdue: Boolean,
    val overdueDays: Long,
    val lastTransactionAt: Long?,
)
