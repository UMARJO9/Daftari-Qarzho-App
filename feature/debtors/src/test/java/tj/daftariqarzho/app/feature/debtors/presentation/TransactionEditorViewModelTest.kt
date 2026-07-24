package tj.daftariqarzho.app.feature.debtors.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import tj.daftariqarzho.app.feature.debtors.FakeDebtorRepository
import tj.daftariqarzho.app.feature.debtors.MainDispatcherRule
import tj.daftariqarzho.app.feature.debtors.domain.model.TransactionType
import tj.daftariqarzho.app.feature.debtors.domain.usecase.AddTransactionUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionEditorViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun viewModel(
        repository: FakeDebtorRepository,
        type: TransactionType = TransactionType.DEBT,
    ) = TransactionEditorViewModel(
        debtorId = 1,
        type = type,
        addTransaction = AddTransactionUseCase(repository) { 0 },
    )

    @Test
    fun `ввод суммы фильтруется до цифр и разделителя`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = viewModel(FakeDebtorRepository())

        vm.onEvent(TransactionEditorEvent.AmountChanged("1a2.5b0"))

        assertEquals("12.50", vm.uiState.value.amountInput)
    }

    @Test
    fun `сохранение пустой суммы показывает ошибку`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeDebtorRepository()
        val vm = viewModel(repository)

        vm.onEvent(TransactionEditorEvent.Save)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.showAmountError)
        assertFalse(vm.uiState.value.isSaved)
        assertTrue(repository.addedTransactions.isEmpty())
    }

    @Test
    fun `сохранение долга парсит сомони в дирамы и закрывает форму`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val repository = FakeDebtorRepository()
            val vm = viewModel(repository, TransactionType.DEBT)

            vm.onEvent(TransactionEditorEvent.AmountChanged("125.50"))
            vm.onEvent(TransactionEditorEvent.CommentChanged("нон"))
            vm.onEvent(TransactionEditorEvent.DueDateChanged(999))
            vm.onEvent(TransactionEditorEvent.Save)
            advanceUntilIdle()

            assertTrue(vm.uiState.value.isSaved)
            val saved = repository.addedTransactions.single()
            assertEquals(-12550L, saved.amountInDirams)
            assertEquals("нон", saved.comment)
            assertEquals(999L, saved.dueDate)
        }

    @Test
    fun `сохранение оплаты даёт положительную сумму`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeDebtorRepository()
        val vm = viewModel(repository, TransactionType.PAYMENT)

        vm.onEvent(TransactionEditorEvent.AmountChanged("50"))
        vm.onEvent(TransactionEditorEvent.Save)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isSaved)
        assertEquals(5000L, repository.addedTransactions.single().amountInDirams)
    }

    @Test
    fun `правка суммы сбрасывает ошибку`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = viewModel(FakeDebtorRepository())

        vm.onEvent(TransactionEditorEvent.Save)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.showAmountError)

        vm.onEvent(TransactionEditorEvent.AmountChanged("10"))
        assertFalse(vm.uiState.value.showAmountError)
    }
}
