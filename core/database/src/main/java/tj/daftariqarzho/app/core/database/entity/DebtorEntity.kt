package tj.daftariqarzho.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Запись должника в таблице `debtors`.
 *
 * @property id первичный ключ (автогенерация).
 * @property name имя должника.
 * @property phone телефон (необязательно).
 * @property note заметка (необязательно).
 * @property createdAt дата создания, epoch-миллисекунды.
 */
@Entity(tableName = "debtors")
data class DebtorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String? = null,
    val note: String? = null,
    val createdAt: Long,
)
