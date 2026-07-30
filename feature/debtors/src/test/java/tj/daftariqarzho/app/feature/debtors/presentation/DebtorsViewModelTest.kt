package tj.daftariqarzho.app.feature.debtors.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import tj.daftariqarzho.app.feature.debtors.FakeDebtorRepository
import tj.daftariqarzho.app.feature.debtors.MainDispatcherRule
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveDebtorSummariesUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveTotalBalanceUseCase
import tj.daftariqarzho.app.feature.debtors.summary

@OptIn(ExperimentalCoroutinesApi::class)
class DebtorsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeDebtorRepository().apply {
        summaries.value = listOf(summary(1, "Ali"), summary(2, "Umed"))
        totalBalance.value = -3000
    }

    private fun viewModel() = DebtorsViewModel(
        observeDebtorSummaries = ObserveDebtorSummariesUseCase(repository),
        observeTotalBalance = ObserveTotalBalanceUseCase(repository),
        defaultDispatcher = mainDispatcherRule.testDispatcher,
    )

    @Test
    fun `после загрузки состояние content с балансом`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = viewModel()
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.debtors.size)
        assertEquals(-3000L, state.totalBalanceInDirams)
    }

    @Test
    fun `поиск фильтрует должников по имени`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = viewModel()
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        vm.onEvent(DebtorsEvent.QueryChanged("al"))
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(1, state.debtors.size)
        assertEquals("Ali", state.debtors.first().debtor.name)
    }
}
