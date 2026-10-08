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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionPeriod
import tj.daftariqarzho.app.feature.transactions.domain.usecase.ObserveTransactionsUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsViewModel(
    private val observeTransactions: ObserveTransactionsUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val period = MutableStateFlow(TransactionPeriod.ALL)

    val uiState: StateFlow<TransactionsUiState> = period
        .flatMapLatest { selectedPeriod ->
            observeTransactions(selectedPeriod)
                .map { groups ->
                    TransactionsUiState(
                        isLoading = false,
                        period = selectedPeriod,
                        groups = groups,
                    )
                }
                .onStart { emit(TransactionsUiState(isLoading = true, period = selectedPeriod)) }
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
}
