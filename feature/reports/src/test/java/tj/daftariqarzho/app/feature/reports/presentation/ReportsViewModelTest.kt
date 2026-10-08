package tj.daftariqarzho.app.feature.reports.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class ReportsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val zone = ZoneId.systemDefault()

    private fun startOf(date: LocalDate) = date.atStartOfDay(zone).toInstant().toEpochMilli()

    private class FakeReportsRepository(
        private val given: Long = 0,
        private val received: Long = 0,
        private val topDebtors: List<ReportDebtor> = emptyList(),
    ) : ReportsRepository {
        val requestedPeriods = mutableListOf<Pair<Long, Long>>()

        override fun observeMonthlyReport(monthStartMillis: Long, nextMonthStartMillis: Long): Flow<MonthlyReport> {
            requestedPeriods += monthStartMillis to nextMonthStartMillis
            return flowOf(MonthlyReport(monthStartMillis, given, received, topDebtors))
        }
    }

    private fun viewModel(repository: ReportsRepository, dayStarts: Flow<Long>) = ReportsViewModel(
        ObserveMonthlyReportUseCase(repository, dayStarts),
        mainDispatcherRule.testDispatcher,
    )

    @Test
    fun `состояние наполняется итогами и топом`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeReportsRepository(
            given = 50000,
            received = 12000,
            topDebtors = listOf(ReportDebtor(1, "Ali", -5000)),
        )
        val vm = viewModel(repository, flowOf(startOf(LocalDate.of(2026, 10, 8))))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals(50000L, state.givenInDirams)
        assertEquals(12000L, state.receivedInDirams)
        assertEquals(1, state.topDebtors.size)
    }

    @Test
    fun `отчёт запрашивается за полуинтервал текущего месяца`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeReportsRepository()
        val vm = viewModel(repository, flowOf(startOf(LocalDate.of(2026, 10, 8))))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        val october = startOf(LocalDate.of(2026, 10, 1))
        assertEquals(listOf(october to startOf(LocalDate.of(2026, 11, 1))), repository.requestedPeriods)
        assertEquals(october, vm.uiState.value.monthStartMillis)
    }

    @Test
    fun `при смене месяца отчёт переключается на новый месяц`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeReportsRepository()
        val dayStarts = MutableStateFlow(startOf(LocalDate.of(2026, 9, 30)))
        val vm = viewModel(repository, dayStarts)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        dayStarts.value = startOf(LocalDate.of(2026, 10, 1))
        advanceUntilIdle()

        assertEquals(startOf(LocalDate.of(2026, 10, 1)), vm.uiState.value.monthStartMillis)
        assertEquals(DateFormatter.startOfNextMonthMillis(dayStarts.value), repository.requestedPeriods.last().second)
    }

    @Test
    fun `новый день того же месяца не перезапрашивает отчёт`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeReportsRepository()
        val dayStarts = MutableStateFlow(startOf(LocalDate.of(2026, 10, 7)))
        val vm = viewModel(repository, dayStarts)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        dayStarts.value = startOf(LocalDate.of(2026, 10, 8))
        advanceUntilIdle()

        assertEquals(1, repository.requestedPeriods.size)
    }
}
