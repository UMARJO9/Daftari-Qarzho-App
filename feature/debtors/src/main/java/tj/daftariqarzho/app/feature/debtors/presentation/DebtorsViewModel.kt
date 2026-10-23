package tj.daftariqarzho.app.feature.debtors.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveDebtorSummariesUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveDebtOverviewUseCase

class DebtorsViewModel(
    observeDebtorSummaries: ObserveDebtorSummariesUseCase,
    observeDebtOverview: ObserveDebtOverviewUseCase,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val query = MutableStateFlow("")

    var searchQuery by mutableStateOf("")
        private set

    val uiState: StateFlow<DebtorsUiState> = combine(
        query,
        observeDebtorSummaries(),
        observeDebtOverview(),
    ) { currentQuery, summaries, overview ->
        val filtered = if (currentQuery.isBlank()) {
            summaries
        } else {
            summaries.filter { it.debtor.name.contains(currentQuery.trim(), ignoreCase = true) }
        }
        DebtorsUiState(
            isLoading = false,
            query = currentQuery,
            debtors = filtered,
            overview = overview,
        )
    }.flowOn(defaultDispatcher).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DebtorsUiState(),
    )

    fun onEvent(event: DebtorsEvent) {
        when (event) {
            is DebtorsEvent.QueryChanged -> {
                searchQuery = event.query
                query.value = event.query
            }
        }
    }
}
