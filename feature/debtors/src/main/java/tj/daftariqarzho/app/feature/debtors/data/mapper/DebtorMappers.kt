package tj.daftariqarzho.app.feature.debtors.data.mapper

import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.core.database.entity.DebtorEntity
import tj.daftariqarzho.app.core.database.entity.DebtorWithBalance
import tj.daftariqarzho.app.feature.debtors.domain.model.Debtor
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorSummary

fun DebtorEntity.toDomain(): Debtor =
    Debtor(
        id = id,
        name = name,
        phone = phone,
        note = note,
        createdAt = createdAt,
    )

fun Debtor.toEntity(): DebtorEntity =
    DebtorEntity(
        id = id,
        name = name,
        phone = phone,
        note = note,
        createdAt = createdAt,
    )

fun DebtorWithBalance.toSummary(nowMillis: Long): DebtorSummary {
    val due = nearestDueDate
    val overdue = balance < 0 && due != null && due < nowMillis
    val overdueDays = if (overdue && due != null) DateFormatter.daysOverdue(due, nowMillis) else 0L
    return DebtorSummary(
        debtor = debtor.toDomain(),
        balanceInDirams = balance,
        isOverdue = overdue,
        overdueDays = overdueDays,
        lastTransactionAt = lastTransactionAt,
    )
}
