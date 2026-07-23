package tj.daftariqarzho.app.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Операция по долгу в таблице `transactions`.
 *
 * Знак [amountInDirams] задаёт тип операции: минус — клиент взял в долг,
 * плюс — клиент вернул / получена оплата. Сумма — в дирамах (minor units).
 * При удалении должника его операции удаляются каскадно.
 *
 * @property id первичный ключ (автогенерация).
 * @property debtorId ссылка на [DebtorEntity].
 * @property amountInDirams сумма в дирамах со знаком.
 * @property comment комментарий (необязательно).
 * @property dueDate срок возврата, epoch-миллисекунды (необязательно).
 * @property photoUri URI фотографии-подтверждения (необязательно).
 * @property createdAt дата операции, epoch-миллисекунды.
 */
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
