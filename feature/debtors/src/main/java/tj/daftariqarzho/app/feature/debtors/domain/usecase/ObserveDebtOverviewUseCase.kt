package tj.daftariqarzho.app.feature.debtors.domain.usecase

import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtOverview
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository

class ObserveDebtOverviewUseCase(
    private val repository: DebtorRepository,
) {
    operator fun invoke(): Flow<DebtOverview> = repository.observeDebtOverview()
}
