package tj.daftariqarzho.app.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.core.database.entity.DebtorEntity
import tj.daftariqarzho.app.core.database.entity.DebtorWithBalance

@Dao
interface DebtorDao {

    @Insert
    suspend fun insert(debtor: DebtorEntity): Long

    @Update
    suspend fun update(debtor: DebtorEntity)

    @Delete
    suspend fun delete(debtor: DebtorEntity)

    @Query("DELETE FROM debtors WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM debtors WHERE id = :id")
    suspend fun getById(id: Long): DebtorEntity?

    @Query("SELECT * FROM debtors WHERE id = :id")
    fun observeById(id: Long): Flow<DebtorEntity?>

    @Query("SELECT * FROM debtors ORDER BY name COLLATE NOCASE")
    fun observeAll(): Flow<List<DebtorEntity>>

    @Query(
        """
        SELECT d.*,
               COALESCE(SUM(t.amountInDirams), 0) AS balance,
               MIN(CASE WHEN t.amountInDirams < 0 AND t.dueDate IS NOT NULL THEN t.dueDate END) AS nearestDueDate,
               MAX(t.createdAt) AS lastTransactionAt
        FROM debtors d
        LEFT JOIN transactions t ON t.debtorId = d.id
        GROUP BY d.id
        ORDER BY d.name COLLATE NOCASE
        """,
    )
    fun observeDebtorsWithBalance(): Flow<List<DebtorWithBalance>>
}
