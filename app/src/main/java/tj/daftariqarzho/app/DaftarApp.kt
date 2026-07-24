package tj.daftariqarzho.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import tj.daftariqarzho.app.core.database.di.databaseModule
import tj.daftariqarzho.app.feature.debtors.di.debtorsModule
import tj.daftariqarzho.app.feature.reports.di.reportsModule
import tj.daftariqarzho.app.feature.settings.di.settingsModule
import tj.daftariqarzho.app.feature.transactions.di.transactionsModule

class DaftarApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DaftarApp)
            modules(
                databaseModule,
                debtorsModule,
                transactionsModule,
                reportsModule,
                settingsModule,
            )
        }
    }
}
