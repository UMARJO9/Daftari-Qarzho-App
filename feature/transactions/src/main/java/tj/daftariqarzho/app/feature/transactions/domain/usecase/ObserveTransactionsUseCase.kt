package tj.daftariqarzho.app.feature.transactions.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.core.common.datetime.DayTicker
import tj.daftariqarzho.app.feature.transactions.domain.model.DayGroup
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionItem
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionPeriod
import tj.daftariqarzho.app.feature.transactions.domain.repository.TransactionsRepository

class ObserveTransactionsUseCase(
    private val repository: TransactionsRepository,
    private val dayStarts: Flow<Long> = DayTicker.dayStarts(),
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(period: TransactionPeriod): Flow<List<DayGroup>> =
        dayStarts
            .map { today -> period.startMillis(today) }
            .distinctUntilChanged()
            .flatMapLatest { from ->
                repository.observeTransactions(from).map { it.groupIntoDays() }
            }

    private fun TransactionPeriod.startMillis(today: Long): Long = when (this) {
        TransactionPeriod.ALL -> 0L
        TransactionPeriod.TODAY -> today
        TransactionPeriod.WEEK -> DateFormatter.startOfWeekMillis(today)
        TransactionPeriod.MONTH -> DateFormatter.startOfMonthMillis(today)
    }

    private fun List<TransactionItem>.groupIntoDays(): List<DayGroup> =
        groupBy { DateFormatter.startOfDayMillis(it.createdAt) }
            .map { (dayMillis, items) -> DayGroup(dayMillis, items) }
            .sortedByDescending { it.dayMillis }
}
