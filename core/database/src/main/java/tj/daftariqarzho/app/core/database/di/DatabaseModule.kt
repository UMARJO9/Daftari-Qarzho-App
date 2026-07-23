package tj.daftariqarzho.app.core.database.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tj.daftariqarzho.app.core.database.AppDatabase

/**
 * Koin-модуль слоя базы данных: предоставляет [AppDatabase] и DAO как
 * синглтоны. Подключается в `startKoin` приложения.
 */
val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.NAME,
        ).build()
    }
    single { get<AppDatabase>().debtorDao() }
    single { get<AppDatabase>().transactionDao() }
}
