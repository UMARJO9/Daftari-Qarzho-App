package tj.daftariqarzho.app.feature.debtors.domain.usecase

import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository

class DeleteDebtorUseCase(
    private val repository: DebtorRepository,
) {
    suspend operator fun invoke(debtorId: Long) {
        repository.deleteDebtor(debtorId)
    }
}
