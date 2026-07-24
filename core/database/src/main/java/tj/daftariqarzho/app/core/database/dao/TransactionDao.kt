package tj.daftariqarzho.app.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.core.database.entity.TransactionEntity
import tj.daftariqarzho.app.core.database.entity.TransactionWithDebtor

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: TransactionEntity): Long

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE debtorId = :debtorId ORDER BY createdAt DESC")
    fun observeByDebtor(debtorId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE createdAt BETWEEN :from AND :to ORDER BY createdAt DESC")
    fun observeBetween(from: Long, to: Long): Flow<List<TransactionEntity>>

    @Query("SELECT COALESCE(SUM(amountInDirams), 0) FROM transactions WHERE debtorId = :debtorId")
    fun observeBalance(debtorId: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountInDirams), 0) FROM transactions")
    fun observeTotalBalance(): Flow<Long>

    @Query(
        """
        SELECT t.*, d.name AS debtorName
        FROM transactions t
        INNER JOIN debtors d ON d.id = t.debtorId
        WHERE t.createdAt >= :from
        ORDER BY t.createdAt DESC
        """,
    )
    fun observeWithDebtorFrom(from: Long): Flow<List<TransactionWithDebtor>>
}
