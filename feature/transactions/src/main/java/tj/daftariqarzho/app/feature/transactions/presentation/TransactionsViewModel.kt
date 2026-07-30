package tj.daftariqarzho.app.feature.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionItem
import tj.daftariqarzho.app.feature.transactions.domain.usecase.ObserveTransactionsUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsViewModel(
    private val observeTransactions: ObserveTransactionsUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val now: () -> Long = System::currentTimeMillis,
) : ViewModel() {

    private val period = MutableStateFlow(TransactionPeriod.ALL)

    val uiState: StateFlow<TransactionsUiState> = period
        .flatMapLatest { selectedPeriod ->
            observeTransactions(fromMillis(selectedPeriod)).map { items ->
                TransactionsUiState(
                    isLoading = false,
                    period = selectedPeriod,
                    groups = items.groupIntoDays(),
                )
            }
        }
        .flowOn(defaultDispatcher)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TransactionsUiState(),
        )

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.PeriodSelected -> period.value = event.period
        }
    }

    private fun fromMillis(period: TransactionPeriod): Long = when (period) {
        TransactionPeriod.ALL -> 0L
        TransactionPeriod.TODAY -> DateFormatter.startOfDayMillis(now())
        TransactionPeriod.WEEK -> DateFormatter.startOfWeekMillis(now())
        TransactionPeriod.MONTH -> DateFormatter.startOfMonthMillis(now())
    }

    private fun List<TransactionItem>.groupIntoDays(): List<DayGroup> =
        groupBy { DateFormatter.startOfDayMillis(it.createdAt) }
            .map { (dayMillis, items) -> DayGroup(dayMillis, items) }
            .sortedByDescending { it.dayMillis }
}
