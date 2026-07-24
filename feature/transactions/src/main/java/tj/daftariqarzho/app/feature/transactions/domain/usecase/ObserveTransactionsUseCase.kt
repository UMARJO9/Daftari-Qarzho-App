package tj.daftariqarzho.app.feature.transactions.domain.usecase

import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionItem
import tj.daftariqarzho.app.feature.transactions.domain.repository.TransactionsRepository

class ObserveTransactionsUseCase(
    private val repository: TransactionsRepository,
) {
    operator fun invoke(fromMillis: Long): Flow<List<TransactionItem>> =
        repository.observeTransactions(fromMillis)
}
