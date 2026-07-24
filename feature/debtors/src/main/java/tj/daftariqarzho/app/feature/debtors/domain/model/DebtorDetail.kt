package tj.daftariqarzho.app.feature.debtors.domain.model

data class DebtorDetail(
    val debtor: Debtor,
    val balanceInDirams: Long,
    val transactions: List<DebtTransaction>,
)
