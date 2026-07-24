package tj.daftariqarzho.app.feature.debtors.domain.repository

import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.feature.debtors.domain.model.Debtor
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorSummary

interface DebtorRepository {
    fun observeDebtorSummaries(): Flow<List<DebtorSummary>>
    fun observeTotalBalance(): Flow<Long>
    suspend fun getDebtor(id: Long): Debtor?
    suspend fun addDebtor(debtor: Debtor): Long
    suspend fun updateDebtor(debtor: Debtor)
}
