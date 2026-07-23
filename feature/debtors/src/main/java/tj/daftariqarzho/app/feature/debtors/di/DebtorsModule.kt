package tj.daftariqarzho.app.feature.debtors.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tj.daftariqarzho.app.feature.debtors.data.repository.DebtorRepositoryImpl
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveDebtorSummariesUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveTotalBalanceUseCase
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorsViewModel

val debtorsModule = module {
    single<DebtorRepository> { DebtorRepositoryImpl(get(), get()) }
    factory { ObserveDebtorSummariesUseCase(get()) }
    factory { ObserveTotalBalanceUseCase(get()) }
    viewModel { DebtorsViewModel(get(), get()) }
}
