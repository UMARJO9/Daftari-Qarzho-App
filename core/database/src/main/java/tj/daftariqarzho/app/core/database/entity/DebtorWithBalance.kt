package tj.daftariqarzho.app.core.database.entity

import androidx.room.Embedded

data class DebtorWithBalance(
    @Embedded val debtor: DebtorEntity,
    val balance: Long,
    val nearestDueDate: Long?,
)
