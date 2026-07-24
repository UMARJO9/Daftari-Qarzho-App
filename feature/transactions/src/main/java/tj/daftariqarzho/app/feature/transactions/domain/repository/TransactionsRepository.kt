package tj.daftariqarzho.app.feature.transactions.domain.repository

import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionItem

interface TransactionsRepository {
    fun observeTransactions(fromMillis: Long): Flow<List<TransactionItem>>
}
