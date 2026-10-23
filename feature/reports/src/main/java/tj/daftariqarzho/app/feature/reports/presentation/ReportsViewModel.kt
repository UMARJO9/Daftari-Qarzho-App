package tj.daftariqarzho.app.feature.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tj.daftariqarzho.app.feature.reports.domain.usecase.ObserveMonthlyReportUseCase

class ReportsViewModel(
    observeMonthlyReport: ObserveMonthlyReportUseCase,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    val uiState: StateFlow<ReportsUiState> = observeMonthlyReport()
        .map { report ->
            ReportsUiState(
                isLoading = false,
                monthStartMillis = report.monthStartMillis,
                givenInDirams = report.givenInDirams,
                receivedInDirams = report.receivedInDirams,
                topDebtors = report.topDebtors,
            )
        }
        .flowOn(defaultDispatcher)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ReportsUiState(),
        )
}
