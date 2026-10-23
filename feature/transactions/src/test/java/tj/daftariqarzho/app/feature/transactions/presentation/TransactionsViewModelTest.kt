package tj.daftariqarzho.app.feature.transactions.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.feature.transactions.FakeTransactionsRepository
import tj.daftariqarzho.app.feature.transactions.MainDispatcherRule
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionItem
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionPeriod
import tj.daftariqarzho.app.feature.transactions.domain.repository.TransactionsRepository
import tj.daftariqarzho.app.feature.transactions.domain.usecase.ObserveTransactionsUseCase
import tj.daftariqarzho.app.feature.transactions.transactionItem

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val day = 86_400_000L
    private val noon = 12 * 3_600_000L
    private val today = DateFormatter.startOfDayMillis(200 * day + noon)
    private val dayStarts = MutableStateFlow(today)

    private fun viewModel(repository: TransactionsRepository) =
        TransactionsViewModel(
            ObserveTransactionsUseCase(repository, dayStarts),
            mainDispatcherRule.testDispatcher,
        )

    @Test
    fun `операции группируются по дням в порядке убывания`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeTransactionsRepository(
            items = listOf(
                transactionItem(1, "Ali", -1000, createdAt = 105 * day + noon),
                transactionItem(2, "Vali", 500, createdAt = 100 * day + noon),
                transactionItem(3, "Umed", -300, createdAt = 100 * day + noon + 3_600_000L),
            ),
        )
        val vm = viewModel(repository)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.groups.size)
        assertTrue(state.groups[0].dayMillis > state.groups[1].dayMillis)
        assertEquals(1, state.groups[0].items.size)
        assertEquals(2, state.groups[1].items.size)
        assertEquals(200L, state.groups[1].netInDirams)
    }

    @Test
    fun `период ALL запрашивает операции с начала времён`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeTransactionsRepository()
        val vm = viewModel(repository)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        assertEquals(0L, repository.lastRequestedFrom)
    }

    @Test
    fun `выбор периода МЕСЯЦ запрашивает с начала месяца`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeTransactionsRepository()
        val vm = viewModel(repository)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        vm.onEvent(TransactionsEvent.PeriodSelected(TransactionPeriod.MONTH))
        advanceUntilIdle()

        assertEquals(TransactionPeriod.MONTH, vm.uiState.value.period)
        assertEquals(DateFormatter.startOfMonthMillis(today), repository.lastRequestedFrom)
    }

    @Test
    fun `выбор периода НЕДЕЛЯ запрашивает с начала недели`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeTransactionsRepository()
        val vm = viewModel(repository)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        vm.onEvent(TransactionsEvent.PeriodSelected(TransactionPeriod.WEEK))
        advanceUntilIdle()

        assertEquals(DateFormatter.startOfWeekMillis(today), repository.lastRequestedFrom)
    }

    @Test
    fun `после полуночи период СЕГОДНЯ переключается на новый день`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeTransactionsRepository()
        val vm = viewModel(repository)
        backgroundScope.launch { vm.uiState.collect {} }
        vm.onEvent(TransactionsEvent.PeriodSelected(TransactionPeriod.TODAY))
        advanceUntilIdle()
        assertEquals(today, repository.lastRequestedFrom)

        val tomorrow = DateFormatter.startOfNextDayMillis(today)
        dayStarts.value = tomorrow
        advanceUntilIdle()

        assertEquals(tomorrow, repository.lastRequestedFrom)
    }

    @Test
    fun `выбранный период отображается сразу, до ответа базы`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = object : TransactionsRepository {
            override fun observeTransactions(fromMillis: Long): Flow<List<TransactionItem>> = emptyFlow()
        }
        val vm = viewModel(repository)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        vm.onEvent(TransactionsEvent.PeriodSelected(TransactionPeriod.WEEK))
        advanceUntilIdle()

        assertEquals(TransactionPeriod.WEEK, vm.uiState.value.period)
        assertTrue(vm.uiState.value.isLoading)
    }

    @Test
    fun `пустой список даёт isEmpty`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeTransactionsRepository(items = emptyList())
        val vm = viewModel(repository)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isEmpty)
    }
}
