package tj.daftariqarzho.app.feature.debtors.data.mapper

import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.core.database.entity.DebtorEntity
import tj.daftariqarzho.app.core.database.entity.DebtorWithBalance
import tj.daftariqarzho.app.core.database.entity.TransactionEntity
import tj.daftariqarzho.app.feature.debtors.domain.model.Debtor
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtTransaction
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
    val overdueDays = due?.takeIf { overdue }?.let { DateFormatter.daysOverdue(it, nowMillis) } ?: 0L
    return DebtorSummary(
        debtor = debtor.toDomain(),
        balanceInDirams = balance,
        isOverdue = overdue,
        overdueDays = overdueDays,
        lastTransactionAt = lastTransactionAt,
    )
}

fun TransactionEntity.toDomain(): DebtTransaction =
    DebtTransaction(
        id = id,
        debtorId = debtorId,
        amountInDirams = amountInDirams,
        comment = comment,
        dueDate = dueDate,
        photoUri = photoUri,
        createdAt = createdAt,
    )

fun DebtTransaction.toEntity(): TransactionEntity =
    TransactionEntity(
        id = id,
        debtorId = debtorId,
        amountInDirams = amountInDirams,
        comment = comment,
        dueDate = dueDate,
        photoUri = photoUri,
        createdAt = createdAt,
    )
