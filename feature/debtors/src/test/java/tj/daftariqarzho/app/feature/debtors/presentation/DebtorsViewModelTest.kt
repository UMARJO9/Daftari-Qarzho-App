package tj.daftariqarzho.app.feature.debtors.presentation

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
import tj.daftariqarzho.app.feature.debtors.MainDispatcherRule
import tj.daftariqarzho.app.feature.debtors.domain.model.Debtor
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorSummary
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveDebtorSummariesUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveTotalBalanceUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class DebtorsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun summary(name: String, id: Long) = DebtorSummary(
        debtor = Debtor(id = id, name = name, phone = null, note = null, createdAt = 0),
        balanceInDirams = -1000,
        isOverdue = false,
        overdueDays = 0,
        lastTransactionAt = null,
    )

    private val repository = object : DebtorRepository {
        override fun observeDebtorSummaries(): Flow<List<DebtorSummary>> =
            flowOf(listOf(summary("Ali", 1), summary("Umed", 2)))

        override fun observeTotalBalance(): Flow<Long> = flowOf(-3000)
    }

    private fun viewModel() = DebtorsViewModel(
        observeDebtorSummaries = ObserveDebtorSummariesUseCase(repository),
        observeTotalBalance = ObserveTotalBalanceUseCase(repository),
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
