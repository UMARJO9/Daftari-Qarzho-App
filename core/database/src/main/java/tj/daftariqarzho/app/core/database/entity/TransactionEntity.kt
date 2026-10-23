package tj.daftariqarzho.app.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = DebtorEntity::class,
            parentColumns = ["id"],
            childColumns = ["debtorId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("debtorId")],
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val debtorId: Long,
    val amountInDirams: Long,
    val comment: String? = null,
    val dueDate: Long? = null,
    val photoUri: String? = null,
    val createdAt: Long,
)
