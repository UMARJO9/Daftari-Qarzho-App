package tj.daftariqarzho.app.feature.debtors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import tj.daftariqarzho.app.feature.debtors.domain.model.Debtor
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorSummary
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository

class FakeDebtorRepository(
    initialDebtors: List<Debtor> = emptyList(),
) : DebtorRepository {

    val summaries = MutableStateFlow<List<DebtorSummary>>(emptyList())
    val totalBalance = MutableStateFlow(0L)
    val storage = initialDebtors.associateBy { it.id }.toMutableMap()

    var nextId: Long = 100
    var addedCount: Int = 0
    var updatedCount: Int = 0

    override fun observeDebtorSummaries(): Flow<List<DebtorSummary>> = summaries

    override fun observeTotalBalance(): Flow<Long> = totalBalance

    override suspend fun getDebtor(id: Long): Debtor? = storage[id]

    override suspend fun addDebtor(debtor: Debtor): Long {
        val id = nextId++
        storage[id] = debtor.copy(id = id)
        addedCount++
        return id
    }

    override suspend fun updateDebtor(debtor: Debtor) {
        storage[debtor.id] = debtor
        updatedCount++
    }
}

fun debtor(
    id: Long,
    name: String,
    phone: String? = null,
    note: String? = null,
    createdAt: Long = 0,
) = Debtor(id = id, name = name, phone = phone, note = note, createdAt = createdAt)

fun summary(
    id: Long,
    name: String,
    balanceInDirams: Long = -1000,
) = DebtorSummary(
    debtor = debtor(id = id, name = name),
    balanceInDirams = balanceInDirams,
    isOverdue = false,
    overdueDays = 0,
    lastTransactionAt = null,
)
