package tj.daftariqarzho.app.feature.debtors.domain.usecase

import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorSummary
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository

class ObserveDebtorSummariesUseCase(
    private val repository: DebtorRepository,
) {
    operator fun invoke(): Flow<List<DebtorSummary>> = repository.observeDebtorSummaries()
}
