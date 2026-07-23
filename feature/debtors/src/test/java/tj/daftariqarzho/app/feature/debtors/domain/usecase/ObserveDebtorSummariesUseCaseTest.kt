package tj.daftariqarzho.app.feature.debtors.domain.usecase

import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import tj.daftariqarzho.app.feature.debtors.domain.model.Debtor
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorSummary
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository

class ObserveDebtorSummariesUseCaseTest {

    private fun summary(name: String, id: Long) = DebtorSummary(
        debtor = Debtor(id = id, name = name, phone = null, note = null, createdAt = 0),
        balanceInDirams = -1000,
        isOverdue = false,
        overdueDays = 0,
        lastTransactionAt = null,
    )

    private val repository = object : DebtorRepository {
        override fun observeDebtorSummaries(): Flow<List<DebtorSummary>> =
            flowOf(listOf(summary("Ali", 1), summary("Vali", 2)))

        override fun observeTotalBalance(): Flow<Long> = flowOf(-2000)
    }

    @Test
    fun `use case отдаёт поток репозитория`() = runTest {
        val useCase = ObserveDebtorSummariesUseCase(repository)
        useCase().test {
            val list = awaitItem()
            assertEquals(2, list.size)
            assertEquals("Ali", list.first().debtor.name)
            awaitComplete()
        }
    }
}
