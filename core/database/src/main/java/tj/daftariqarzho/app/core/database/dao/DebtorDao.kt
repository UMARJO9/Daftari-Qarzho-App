package tj.daftariqarzho.app.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tj.daftariqarzho.app.core.database.entity.DebtorEntity
import tj.daftariqarzho.app.core.database.entity.DebtorWithBalance

/** DAO для доступа к должникам. */
@Dao
interface DebtorDao {

    /** Вставляет должника и возвращает его сгенерированный id. */
    @Insert
    suspend fun insert(debtor: DebtorEntity): Long

    /** Обновляет должника. */
    @Update
    suspend fun update(debtor: DebtorEntity)

    /** Удаляет должника (его операции удаляются каскадно). */
    @Delete
    suspend fun delete(debtor: DebtorEntity)

    /** Возвращает должника по id или `null`. */
    @Query("SELECT * FROM debtors WHERE id = :id")
    suspend fun getById(id: Long): DebtorEntity?

    /** Наблюдает за должником по id. */
    @Query("SELECT * FROM debtors WHERE id = :id")
    fun observeById(id: Long): Flow<DebtorEntity?>

    /** Наблюдает за всеми должниками, отсортированными по имени. */
    @Query("SELECT * FROM debtors ORDER BY name COLLATE NOCASE")
    fun observeAll(): Flow<List<DebtorEntity>>

    /**
     * Наблюдает за списком должников с посчитанным балансом и ближайшим сроком
     * возврата. Баланс считается запросом (не хранится).
     */
    @Query(
        """
        SELECT d.*,
               COALESCE(SUM(t.amountInDirams), 0) AS balance,
               MIN(CASE WHEN t.amountInDirams < 0 AND t.dueDate IS NOT NULL THEN t.dueDate END) AS nearestDueDate
        FROM debtors d
        LEFT JOIN transactions t ON t.debtorId = d.id
        GROUP BY d.id
        ORDER BY d.name COLLATE NOCASE
        """,
    )
    fun observeDebtorsWithBalance(): Flow<List<DebtorWithBalance>>
}
