package tj.daftariqarzho.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tj.daftariqarzho.app.core.database.dao.DebtorDao
import tj.daftariqarzho.app.core.database.dao.TransactionDao
import tj.daftariqarzho.app.core.database.entity.DebtorEntity
import tj.daftariqarzho.app.core.database.entity.TransactionEntity

@Database(
    entities = [DebtorEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun debtorDao(): DebtorDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        const val NAME = "daftar.db"
    }
}
