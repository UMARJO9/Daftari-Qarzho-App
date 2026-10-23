package tj.daftariqarzho.app.feature.debtors.domain.usecase

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import tj.daftariqarzho.app.feature.debtors.FakeDebtorRepository
import tj.daftariqarzho.app.feature.debtors.summary

class ObserveDebtorSummariesUseCaseTest {

    @Test
    fun `use case отдаёт поток репозитория`() = runTest {
        val repository = FakeDebtorRepository()
        repository.summaries.value = listOf(summary(1, "Ali"), summary(2, "Vali"))

        ObserveDebtorSummariesUseCase(repository)().test {
            val list = awaitItem()
            assertEquals(2, list.size)
            assertEquals("Ali", list.first().debtor.name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `use case отдаёт обновления списка`() = runTest {
        val repository = FakeDebtorRepository()

        ObserveDebtorSummariesUseCase(repository)().test {
            assertEquals(0, awaitItem().size)

            repository.summaries.value = listOf(summary(1, "Ali"))
            assertEquals(1, awaitItem().size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
