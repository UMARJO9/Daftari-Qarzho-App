package tj.daftariqarzho.app.feature.transactions.data.mapper

import tj.daftariqarzho.app.core.database.entity.TransactionWithDebtor
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionItem

fun TransactionWithDebtor.toItem(): TransactionItem =
    TransactionItem(
        id = transaction.id,
        debtorId = transaction.debtorId,
        debtorName = debtorName,
        amountInDirams = transaction.amountInDirams,
        comment = transaction.comment,
        createdAt = transaction.createdAt,
    )
