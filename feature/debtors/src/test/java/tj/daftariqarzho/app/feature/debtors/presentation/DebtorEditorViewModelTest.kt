package tj.daftariqarzho.app.feature.debtors.presentation

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import tj.daftariqarzho.app.feature.debtors.FakeDebtorRepository
import tj.daftariqarzho.app.feature.debtors.MainDispatcherRule
import tj.daftariqarzho.app.feature.debtors.debtor
import tj.daftariqarzho.app.feature.debtors.domain.usecase.GetDebtorUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.SaveDebtorUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class DebtorEditorViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun viewModel(repository: FakeDebtorRepository, debtorId: Long? = null) =
        DebtorEditorViewModel(
            debtorId = debtorId,
            getDebtor = GetDebtorUseCase(repository),
            saveDebtor = SaveDebtorUseCase(repository) { 0 },
        )

    @Test
    fun `режим добавления стартует с пустой формой`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = viewModel(FakeDebtorRepository())

        val state = vm.uiState.value
        assertFalse(state.isEditMode)
        assertFalse(state.isLoading)
        assertEquals("", state.name)
        assertFalse(state.canSave)
    }

    @Test
    fun `сохранение с пустым именем показывает ошибку`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeDebtorRepository()
        val vm = viewModel(repository)

        vm.onEvent(DebtorEditorEvent.NameChanged("   "))
        vm.onEvent(DebtorEditorEvent.Save)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(DebtorNameError.EMPTY, state.nameError)
        assertFalse(state.isSaved)
        assertEquals(0, repository.addedCount)
    }

    @Test
    fun `ошибка имени сбрасывается при вводе`() = runTest(mainDispatcherRule.testDispatcher) {
        val vm = viewModel(FakeDebtorRepository())

        vm.onEvent(DebtorEditorEvent.NameChanged(" "))
        vm.onEvent(DebtorEditorEvent.Save)
        advanceUntilIdle()
        assertEquals(DebtorNameError.EMPTY, vm.uiState.value.nameError)

        vm.onEvent(DebtorEditorEvent.NameChanged("А"))
        assertNull(vm.uiState.value.nameError)
    }

    @Test
    fun `успешное добавление закрывает форму`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeDebtorRepository()
        val vm = viewModel(repository)

        vm.uiState.test {
            assertFalse(awaitItem().isSaved)

            vm.onEvent(DebtorEditorEvent.NameChanged("Иброхим"))
            assertEquals("Иброхим", awaitItem().name)

            vm.onEvent(DebtorEditorEvent.PhoneChanged("992900"))
            assertEquals("992900", awaitItem().phone)

            vm.onEvent(DebtorEditorEvent.Save)
            advanceUntilIdle()
            cancelAndIgnoreRemainingEvents()
        }

        val state = vm.uiState.value
        assertTrue(state.isSaved)
        assertFalse(state.isSaving)

        assertEquals(1, repository.addedCount)
        val saved = repository.storage.values.single()
        assertEquals("Иброхим", saved.name)
        assertEquals("992900", saved.phone)
    }

    @Test
    fun `режим редактирования подставляет данные клиента`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeDebtorRepository(
            listOf(debtor(id = 5, name = "Малика", phone = "900", note = "нон")),
        )
        val vm = viewModel(repository, debtorId = 5)
        assertTrue(vm.uiState.value.isLoading)

        advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state.isEditMode)
        assertFalse(state.isLoading)
        assertEquals("Малика", state.name)
        assertEquals("900", state.phone)
        assertEquals("нон", state.note)
        assertTrue(state.canSave)
    }

    @Test
    fun `редактирование обновляет клиента и закрывает форму`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeDebtorRepository(listOf(debtor(id = 5, name = "Малика")))
        val vm = viewModel(repository, debtorId = 5)
        advanceUntilIdle()

        vm.onEvent(DebtorEditorEvent.NameChanged("Малика Шарипова"))
        vm.onEvent(DebtorEditorEvent.Save)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isSaved)
        assertEquals(1, repository.updatedCount)
        assertEquals("Малика Шарипова", repository.storage.getValue(5).name)
    }
}
