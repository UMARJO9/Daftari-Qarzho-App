package tj.daftariqarzho.app.feature.debtors.domain.usecase

import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository

class ObserveTotalBalanceUseCase(
    private val repository: DebtorRepository,
) {
    operator fun invoke(): Flow<Long> = repository.observeTotalBalance()
}
