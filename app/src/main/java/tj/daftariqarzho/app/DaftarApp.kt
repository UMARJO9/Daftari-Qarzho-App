package tj.daftariqarzho.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import tj.daftariqarzho.app.core.database.di.databaseModule

class DaftarApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DaftarApp)
            modules(databaseModule)
        }
    }
}
