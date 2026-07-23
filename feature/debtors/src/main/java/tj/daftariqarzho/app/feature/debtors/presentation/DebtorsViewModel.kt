package tj.daftariqarzho.app.feature.debtors.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveDebtorSummariesUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveTotalBalanceUseCase

class DebtorsViewModel(
    observeDebtorSummaries: ObserveDebtorSummariesUseCase,
    observeTotalBalance: ObserveTotalBalanceUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")

    val uiState: StateFlow<DebtorsUiState> = combine(
        query,
        observeDebtorSummaries(),
        observeTotalBalance(),
    ) { currentQuery, summaries, total ->
        val filtered = if (currentQuery.isBlank()) {
            summaries
        } else {
            summaries.filter { it.debtor.name.contains(currentQuery.trim(), ignoreCase = true) }
        }
        DebtorsUiState(
            isLoading = false,
            query = currentQuery,
            debtors = filtered,
            totalBalanceInDirams = total,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DebtorsUiState(),
    )

    fun onEvent(event: DebtorsEvent) {
        when (event) {
            is DebtorsEvent.QueryChanged -> query.value = event.query
        }
    }
}
