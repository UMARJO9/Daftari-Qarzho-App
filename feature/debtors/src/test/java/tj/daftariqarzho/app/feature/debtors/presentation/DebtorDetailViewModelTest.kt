package tj.daftariqarzho.app.feature.debtors.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import tj.daftariqarzho.app.feature.debtors.FakeDebtorRepository
import tj.daftariqarzho.app.feature.debtors.MainDispatcherRule
import tj.daftariqarzho.app.feature.debtors.debtor
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtTransaction
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorDetail
import tj.daftariqarzho.app.feature.debtors.domain.usecase.DeleteDebtorUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveDebtorDetailUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class DebtorDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun viewModel(repository: FakeDebtorRepository, debtorId: Long = 1) =
        DebtorDetailViewModel(
            debtorId = debtorId,
            observeDebtorDetail = ObserveDebtorDetailUseCase(repository),
            deleteDebtor = DeleteDebtorUseCase(repository),
            defaultDispatcher = mainDispatcherRule.testDispatcher,
        )

    private fun transaction(id: Long, amount: Long) = DebtTransaction(
        id = id,
        debtorId = 1,
        amountInDirams = amount,
        comment = null,
        dueDate = null,
        photoUri = null,
        createdAt = 0,
    )

    @Test
    fun `состояние наполняется данными детали`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeDebtorRepository()
        repository.details.value = DebtorDetail(
            debtor = debtor(id = 1, name = "Иброхим"),
            balanceInDirams = -12500,
            transactions = listOf(transaction(1, -12500)),
        )
        val vm = viewModel(repository)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.notFound)
        assertEquals("Иброхим", state.debtor?.name)
        assertEquals(-12500L, state.balanceInDirams)
        assertEquals(1, state.transactions.size)
    }

    @Test
    fun `отсутствующий должник даёт notFound`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeDebtorRepository()
        repository.details.value = null
        val vm = viewModel(repository)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.notFound)
    }

    @Test
    fun `пустой список операций даёт isEmpty`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeDebtorRepository()
        repository.details.value = DebtorDetail(
            debtor = debtor(id = 1, name = "Малика"),
            balanceInDirams = 0,
            transactions = emptyList(),
        )
        val vm = viewModel(repository)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isEmpty)
    }

    @Test
    fun `удаление вызывает репозиторий и выставляет isDeleted`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeDebtorRepository(listOf(debtor(id = 1, name = "Малика")))
        repository.details.value = DebtorDetail(
            debtor = debtor(id = 1, name = "Малика"),
            balanceInDirams = 0,
            transactions = emptyList(),
        )
        val vm = viewModel(repository, debtorId = 1)
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()

        vm.onEvent(DebtorDetailEvent.Delete)
        advanceUntilIdle()

        assertTrue(vm.isDeleted.value)
        assertEquals(listOf(1L), repository.deletedIds)
    }
}
