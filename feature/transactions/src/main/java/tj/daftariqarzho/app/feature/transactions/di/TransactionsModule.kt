package tj.daftariqarzho.app.feature.transactions.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tj.daftariqarzho.app.feature.transactions.data.repository.TransactionsRepositoryImpl
import tj.daftariqarzho.app.feature.transactions.domain.repository.TransactionsRepository
import tj.daftariqarzho.app.feature.transactions.domain.usecase.ObserveTransactionsUseCase
import tj.daftariqarzho.app.feature.transactions.presentation.TransactionsViewModel

val transactionsModule = module {
    single<TransactionsRepository> { TransactionsRepositoryImpl(get()) }
    factory { ObserveTransactionsUseCase(get()) }
    viewModel { TransactionsViewModel(get()) }
}
