package tj.daftariqarzho.app.feature.debtors.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import tj.daftariqarzho.app.feature.debtors.FakeDebtorRepository
import tj.daftariqarzho.app.feature.debtors.domain.model.TransactionType

class AddTransactionUseCaseTest {

    private val now = 1_700_000_000_000L

    private fun useCase(repository: FakeDebtorRepository) =
        AddTransactionUseCase(repository) { now }

    @Test
    fun `долг сохраняется с отрицательной суммой`() = runTest {
        val repository = FakeDebtorRepository()
        val result = useCase(repository)(
            debtorId = 1,
            type = TransactionType.DEBT,
            amountInDirams = 12500,
            comment = " продукты ",
            dueDate = 999,
        )

        assertTrue(result is AddTransactionResult.Success)
        val saved = repository.addedTransactions.single()
        assertEquals(1L, saved.debtorId)
        assertEquals(-12500L, saved.amountInDirams)
        assertEquals("продукты", saved.comment)
        assertEquals(999L, saved.dueDate)
        assertEquals(now, saved.createdAt)
    }

    @Test
    fun `оплата сохраняется с положительной суммой`() = runTest {
        val repository = FakeDebtorRepository()
        val result = useCase(repository)(
            debtorId = 2,
            type = TransactionType.PAYMENT,
            amountInDirams = 5000,
            comment = null,
            dueDate = null,
        )

        assertTrue(result is AddTransactionResult.Success)
        val saved = repository.addedTransactions.single()
        assertEquals(5000L, saved.amountInDirams)
        assertNull(saved.comment)
        assertNull(saved.dueDate)
    }

    @Test
    fun `пустой комментарий сохраняется как null`() = runTest {
        val repository = FakeDebtorRepository()
        useCase(repository)(
            debtorId = 1,
            type = TransactionType.DEBT,
            amountInDirams = 100,
            comment = "   ",
            dueDate = null,
        )
        assertNull(repository.addedTransactions.single().comment)
    }

    @Test
    fun `нулевая сумма отклоняется`() = runTest {
        val repository = FakeDebtorRepository()
        val result = useCase(repository)(
            debtorId = 1,
            type = TransactionType.DEBT,
            amountInDirams = 0,
            comment = null,
            dueDate = null,
        )

        assertEquals(AddTransactionResult.InvalidAmount, result)
        assertTrue(repository.addedTransactions.isEmpty())
    }

    @Test
    fun `отрицательная сумма отклоняется`() = runTest {
        val repository = FakeDebtorRepository()
        val result = useCase(repository)(
            debtorId = 1,
            type = TransactionType.PAYMENT,
            amountInDirams = -100,
            comment = null,
            dueDate = null,
        )

        assertEquals(AddTransactionResult.InvalidAmount, result)
        assertTrue(repository.addedTransactions.isEmpty())
    }
}
