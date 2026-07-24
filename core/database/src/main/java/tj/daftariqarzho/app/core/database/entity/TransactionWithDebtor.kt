package tj.daftariqarzho.app.core.database.entity

import androidx.room.Embedded

data class TransactionWithDebtor(
    @Embedded val transaction: TransactionEntity,
    val debtorName: String,
)
