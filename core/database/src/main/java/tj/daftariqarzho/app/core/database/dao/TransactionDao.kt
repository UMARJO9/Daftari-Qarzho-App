package tj.daftariqarzho.app.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.core.database.entity.TransactionEntity

/** DAO для доступа к операциям по долгам. */
@Dao
interface TransactionDao {

    /** Вставляет операцию и возвращает её сгенерированный id. */
    @Insert
    suspend fun insert(transaction: TransactionEntity): Long

    /** Обновляет операцию. */
    @Update
    suspend fun update(transaction: TransactionEntity)

    /** Удаляет операцию. */
    @Delete
    suspend fun delete(transaction: TransactionEntity)

    /** Наблюдает за операциями должника (новые сверху). */
    @Query("SELECT * FROM transactions WHERE debtorId = :debtorId ORDER BY createdAt DESC")
    fun observeByDebtor(debtorId: Long): Flow<List<TransactionEntity>>

    /** Наблюдает за всеми операциями (новые сверху). */
    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    /** Наблюдает за операциями за период [from]..[to] (epoch-миллисекунды). */
    @Query("SELECT * FROM transactions WHERE createdAt BETWEEN :from AND :to ORDER BY createdAt DESC")
    fun observeBetween(from: Long, to: Long): Flow<List<TransactionEntity>>

    /** Наблюдает за балансом конкретного должника (сумма операций в дирамах). */
    @Query("SELECT COALESCE(SUM(amountInDirams), 0) FROM transactions WHERE debtorId = :debtorId")
    fun observeBalance(debtorId: Long): Flow<Long>

    /** Наблюдает за суммарным балансом по всем должникам. */
    @Query("SELECT COALESCE(SUM(amountInDirams), 0) FROM transactions")
    fun observeTotalBalance(): Flow<Long>
}
