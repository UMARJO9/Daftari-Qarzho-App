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
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.feature.reports.domain.usecase.ObserveMonthlyReportUseCase

class ReportsViewModel(
    observeMonthlyReport: ObserveMonthlyReportUseCase,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    now: () -> Long = System::currentTimeMillis,
) : ViewModel() {

    private val monthStart = DateFormatter.startOfMonthMillis(now())

    val uiState: StateFlow<ReportsUiState> = observeMonthlyReport(monthStart)
        .map { report ->
            ReportsUiState(
                isLoading = false,
                monthStartMillis = monthStart,
                givenInDirams = report.givenInDirams,
                receivedInDirams = report.receivedInDirams,
                topDebtors = report.topDebtors,
            )
        }
        .flowOn(defaultDispatcher)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ReportsUiState(monthStartMillis = monthStart),
        )
}
