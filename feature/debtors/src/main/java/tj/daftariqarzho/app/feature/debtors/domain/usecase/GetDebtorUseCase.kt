package tj.daftariqarzho.app.feature.debtors.domain.usecase

import tj.daftariqarzho.app.feature.debtors.domain.model.Debtor
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository

class GetDebtorUseCase(
    private val repository: DebtorRepository,
) {
    suspend operator fun invoke(id: Long): Debtor? = repository.getDebtor(id)
}
