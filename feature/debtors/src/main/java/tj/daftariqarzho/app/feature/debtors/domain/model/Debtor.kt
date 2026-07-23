package tj.daftariqarzho.app.feature.debtors.domain.model

data class Debtor(
    val id: Long,
    val name: String,
    val phone: String?,
    val note: String?,
    val createdAt: Long,
)
