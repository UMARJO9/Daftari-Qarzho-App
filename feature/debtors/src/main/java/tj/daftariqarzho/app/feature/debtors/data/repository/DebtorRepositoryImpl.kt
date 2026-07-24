package tj.daftariqarzho.app.feature.debtors.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import tj.daftariqarzho.app.core.database.dao.DebtorDao
import tj.daftariqarzho.app.core.database.dao.TransactionDao
import tj.daftariqarzho.app.feature.debtors.data.mapper.toDomain
import tj.daftariqarzho.app.feature.debtors.data.mapper.toEntity
import tj.daftariqarzho.app.feature.debtors.data.mapper.toSummary
import tj.daftariqarzho.app.feature.debtors.domain.model.Debtor
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorDetail
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorSummary
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository

class DebtorRepositoryImpl(
    private val debtorDao: DebtorDao,
    private val transactionDao: TransactionDao,
    private val now: () -> Long = System::currentTimeMillis,
) : DebtorRepository {

    override fun observeDebtorSummaries(): Flow<List<DebtorSummary>> =
        debtorDao.observeDebtorsWithBalance().map { list ->
            val nowMillis = now()
            list.map { it.toSummary(nowMillis) }
        }

    override fun observeTotalBalance(): Flow<Long> =
        transactionDao.observeTotalBalance()

    override fun observeDebtorDetail(id: Long): Flow<DebtorDetail?> =
        combine(
            debtorDao.observeById(id),
            transactionDao.observeByDebtor(id),
        ) { debtorEntity, transactionEntities ->
            debtorEntity?.let { entity ->
                val transactions = transactionEntities.map { it.toDomain() }
                DebtorDetail(
                    debtor = entity.toDomain(),
                    balanceInDirams = transactions.sumOf { it.amountInDirams },
                    transactions = transactions,
                )
            }
        }

    override suspend fun getDebtor(id: Long): Debtor? =
        debtorDao.getById(id)?.toDomain()

    override suspend fun addDebtor(debtor: Debtor): Long =
        debtorDao.insert(debtor.toEntity())

    override suspend fun updateDebtor(debtor: Debtor) {
        debtorDao.update(debtor.toEntity())
    }

    override suspend fun deleteDebtor(id: Long) {
        debtorDao.deleteById(id)
    }
}
