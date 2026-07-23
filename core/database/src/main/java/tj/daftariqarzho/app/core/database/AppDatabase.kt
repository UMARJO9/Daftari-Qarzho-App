package tj.daftariqarzho.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tj.daftariqarzho.app.core.database.dao.DebtorDao
import tj.daftariqarzho.app.core.database.dao.TransactionDao
import tj.daftariqarzho.app.core.database.entity.DebtorEntity
import tj.daftariqarzho.app.core.database.entity.TransactionEntity

/**
 * Основная база данных приложения (Room).
 *
 * Версия 1 — стартовая схема. Стратегия миграций: схема экспортируется в
 * каталог `schemas/` (`room.schemaLocation`); при повышении версии добавляются
 * объекты [androidx.room.migration.Migration] через `addMigrations(...)`.
 * Деструктивная миграция в продакшене не используется — данные долговой
 * тетради нельзя терять.
 */
@Database(
    entities = [DebtorEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun debtorDao(): DebtorDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        /** Имя файла базы данных. */
        const val NAME = "daftar.db"
    }
}
