package tj.daftariqarzho.app.feature.reports.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tj.daftariqarzho.app.feature.reports.data.repository.ReportsRepositoryImpl
import tj.daftariqarzho.app.feature.reports.domain.repository.ReportsRepository
import tj.daftariqarzho.app.feature.reports.domain.usecase.ObserveMonthlyReportUseCase
import tj.daftariqarzho.app.feature.reports.presentation.ReportsViewModel

val reportsModule = module {
    single<ReportsRepository> { ReportsRepositoryImpl(get(), get()) }
    factory { ObserveMonthlyReportUseCase(get()) }
    viewModel { ReportsViewModel(get()) }
}
