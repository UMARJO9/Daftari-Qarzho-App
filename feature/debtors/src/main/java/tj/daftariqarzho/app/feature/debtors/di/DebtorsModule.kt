package tj.daftariqarzho.app.feature.debtors.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tj.daftariqarzho.app.feature.debtors.data.repository.DebtorRepositoryImpl
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository
import tj.daftariqarzho.app.feature.debtors.domain.usecase.DeleteDebtorUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.GetDebtorUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveDebtorDetailUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveDebtorSummariesUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveTotalBalanceUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.SaveDebtorUseCase
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorDetailViewModel
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorEditorViewModel
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorsViewModel

val debtorsModule = module {
    single<DebtorRepository> { DebtorRepositoryImpl(get(), get()) }
    factory { ObserveDebtorSummariesUseCase(get()) }
    factory { ObserveTotalBalanceUseCase(get()) }
    factory { ObserveDebtorDetailUseCase(get()) }
    factory { GetDebtorUseCase(get()) }
    factory { SaveDebtorUseCase(get()) }
    factory { DeleteDebtorUseCase(get()) }
    viewModel { DebtorsViewModel(get(), get()) }
    viewModel { params -> DebtorEditorViewModel(params.getOrNull<Long>(), get(), get()) }
    viewModel { params -> DebtorDetailViewModel(params.get<Long>(), get(), get()) }
}
