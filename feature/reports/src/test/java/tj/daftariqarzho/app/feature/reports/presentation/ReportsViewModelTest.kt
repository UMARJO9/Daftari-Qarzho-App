package tj.daftariqarzho.app.feature.reports.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.feature.reports.MainDispatcherRule
import tj.daftariqarzho.app.feature.reports.domain.model.MonthlyReport
import tj.daftariqarzho.app.feature.reports.domain.model.ReportDebtor
import tj.daftariqarzho.app.feature.reports.domain.repository.ReportsRepository
import tj.daftariqarzho.app.feature.reports.domain.usecase.ObserveMonthlyReportUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class ReportsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val day = 86_400_000L
    private val now = 200 * day + 12 * 3_600_000L

    private class FakeReportsRepository(private val report: MonthlyReport) : ReportsRepository {
        var requestedFrom: Long? = null
        override fun observeMonthlyReport(fromMillis: Long): Flow<MonthlyReport> {
            requestedFrom = fromMillis
            return flowOf(report)
        }
    }

    @Test
    fun `состояние наполняется итогами и топом`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeReportsRepository(
            MonthlyReport(
                givenInDirams = 50000,
                receivedInDirams = 12000,
                topDebtors = listOf(ReportDebtor(1, "Ali", -5000)),
            ),
        )
        val vm = ReportsViewModel(
            ObserveMonthlyReportUseCase(repository),
            mainDispatcherRule.testDispatcher,
        ) { now }
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals(50000L, state.givenInDirams)
        assertEquals(12000L, state.receivedInDirams)
        assertEquals(1, state.topDebtors.size)
    }

    @Test
    fun `отчёт запрашивается с начала текущего месяца`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeReportsRepository(MonthlyReport(0, 0, emptyList()))
        val vm = ReportsViewModel(
            ObserveMonthlyReportUseCase(repository),
            mainDispatcherRule.testDispatcher,
        ) { now }
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        assertEquals(DateFormatter.startOfMonthMillis(now), repository.requestedFrom)
        assertEquals(DateFormatter.startOfMonthMillis(now), vm.uiState.value.monthStartMillis)
    }
}
