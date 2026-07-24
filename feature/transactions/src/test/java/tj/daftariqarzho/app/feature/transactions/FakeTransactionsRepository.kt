package tj.daftariqarzho.app.feature.transactions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionItem
import tj.daftariqarzho.app.feature.transactions.domain.repository.TransactionsRepository

class FakeTransactionsRepository(
    var items: List<TransactionItem> = emptyList(),
) : TransactionsRepository {

    var lastRequestedFrom: Long? = null

    override fun observeTransactions(fromMillis: Long): Flow<List<TransactionItem>> {
        lastRequestedFrom = fromMillis
        return flowOf(items)
    }
}

fun transactionItem(
    id: Long,
    debtorName: String,
    amountInDirams: Long,
    createdAt: Long,
    comment: String? = null,
    debtorId: Long = 1,
) = TransactionItem(
    id = id,
    debtorId = debtorId,
    debtorName = debtorName,
    amountInDirams = amountInDirams,
    comment = comment,
    createdAt = createdAt,
)
