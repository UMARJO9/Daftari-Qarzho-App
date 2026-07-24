package tj.daftariqarzho.app.feature.debtors.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import tj.daftariqarzho.app.feature.debtors.FakeDebtorRepository
import tj.daftariqarzho.app.feature.debtors.debtor

class SaveDebtorUseCaseTest {

    private val now = 1_700_000_000_000L

    private fun useCase(repository: FakeDebtorRepository) =
        SaveDebtorUseCase(repository) { now }

    @Test
    fun `пустое имя не сохраняется`() = runTest {
        val repository = FakeDebtorRepository()
        val result = useCase(repository)(id = null, name = "", phone = null, note = null)

        assertEquals(SaveDebtorResult.EmptyName, result)
        assertEquals(0, repository.addedCount)
    }

    @Test
    fun `имя из одних пробелов не сохраняется`() = runTest {
        val repository = FakeDebtorRepository()
        val result = useCase(repository)(id = null, name = "   ", phone = null, note = null)

        assertEquals(SaveDebtorResult.EmptyName, result)
        assertEquals(0, repository.addedCount)
    }

    @Test
    fun `слишком длинное имя не сохраняется`() = runTest {
        val repository = FakeDebtorRepository()
        val longName = "а".repeat(SaveDebtorUseCase.MAX_NAME_LENGTH + 1)
        val result = useCase(repository)(id = null, name = longName, phone = null, note = null)

        assertEquals(SaveDebtorResult.NameTooLong, result)
        assertEquals(0, repository.addedCount)
    }

    @Test
    fun `имя максимальной длины сохраняется`() = runTest {
        val repository = FakeDebtorRepository()
        val maxName = "а".repeat(SaveDebtorUseCase.MAX_NAME_LENGTH)
        val result = useCase(repository)(id = null, name = maxName, phone = null, note = null)

        assertTrue(result is SaveDebtorResult.Success)
        assertEquals(1, repository.addedCount)
    }

    @Test
    fun `новый клиент сохраняется с обрезкой пробелов и временем создания`() = runTest {
        val repository = FakeDebtorRepository()
        val result = useCase(repository)(
            id = null,
            name = "  Иброхим  ",
            phone = "  ",
            note = " продукты ",
        )

        val id = (result as SaveDebtorResult.Success).debtorId
        val saved = repository.storage.getValue(id)
        assertEquals("Иброхим", saved.name)
        assertNull(saved.phone)
        assertEquals("продукты", saved.note)
        assertEquals(now, saved.createdAt)
        assertEquals(1, repository.addedCount)
    }

    @Test
    fun `редактирование сохраняет дату создания и id`() = runTest {
        val repository = FakeDebtorRepository(
            listOf(debtor(id = 7, name = "Малика", phone = "900", createdAt = 111)),
        )
        val result = useCase(repository)(
            id = 7,
            name = "Малика Шарипова",
            phone = "992900",
            note = null,
        )

        assertEquals(SaveDebtorResult.Success(7), result)
        val saved = repository.storage.getValue(7)
        assertEquals(7L, saved.id)
        assertEquals("Малика Шарипова", saved.name)
        assertEquals("992900", saved.phone)
        assertNull(saved.note)
        assertEquals(111L, saved.createdAt)
        assertEquals(1, repository.updatedCount)
        assertEquals(0, repository.addedCount)
    }

    @Test
    fun `редактирование несуществующего клиента возвращает NotFound`() = runTest {
        val repository = FakeDebtorRepository()
        val result = useCase(repository)(id = 42, name = "Ном", phone = null, note = null)

        assertEquals(SaveDebtorResult.NotFound, result)
        assertEquals(0, repository.updatedCount)
    }
}
