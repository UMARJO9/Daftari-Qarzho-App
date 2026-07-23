package tj.daftariqarzho.app.core.database.entity

import androidx.room.Embedded

/**
 * Проекция «должник + агрегированный баланс» для списка клиентов.
 *
 * @property debtor сам должник.
 * @property balance сумма всех его операций в дирамах (0, если операций нет).
 * @property nearestDueDate ближайший срок возврата среди непогашенных долгов
 *   (`null`, если сроков нет) — используется для отметки просрочки.
 */
data class DebtorWithBalance(
    @Embedded val debtor: DebtorEntity,
    val balance: Long,
    val nearestDueDate: Long?,
)
