package tj.daftariqarzho.app.feature.transactions.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tj.daftariqarzho.app.core.database.dao.TransactionDao
import tj.daftariqarzho.app.feature.transactions.data.mapper.toItem
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionItem
import tj.daftariqarzho.app.feature.transactions.domain.repository.TransactionsRepository

class TransactionsRepositoryImpl(
    private val transactionDao: TransactionDao,
) : TransactionsRepository {

    override fun observeTransactions(fromMillis: Long): Flow<List<TransactionItem>> =
        transactionDao.observeWithDebtorFrom(fromMillis).map { list ->
            list.map { it.toItem() }
        }
}
