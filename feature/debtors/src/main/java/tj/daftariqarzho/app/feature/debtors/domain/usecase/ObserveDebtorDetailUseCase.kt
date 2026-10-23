package tj.daftariqarzho.app.feature.debtors.domain.usecase

import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorDetail
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository

class ObserveDebtorDetailUseCase(
    private val repository: DebtorRepository,
) {
    operator fun invoke(debtorId: Long): Flow<DebtorDetail?> =
        repository.observeDebtorDetail(debtorId)
}
