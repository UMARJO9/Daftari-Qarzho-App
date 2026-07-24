package tj.daftariqarzho.app.feature.reports.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import tj.daftariqarzho.app.core.database.entity.DebtorEntity
import tj.daftariqarzho.app.core.database.entity.DebtorWithBalance

class ReportMappersTest {

    private fun row(id: Long, name: String, balance: Long) = DebtorWithBalance(
        debtor = DebtorEntity(id = id, name = name, phone = null, note = null, createdAt = 0),
        balance = balance,
        nearestDueDate = null,
        lastTransactionAt = null,
    )

    @Test
    fun `в топ попадают только должники с отрицательным балансом`() {
        val result = listOf(
            row(1, "Ali", -1000),
            row(2, "Vali", 500),
            row(3, "Umed", 0),
        ).toTopDebtors()

        assertEquals(1, result.size)
        assertEquals("Ali", result.first().name)
    }

    @Test
    fun `сортировка по величине долга — самый большой первым`() {
        val result = listOf(
            row(1, "Ali", -1000),
            row(2, "Vali", -5000),
            row(3, "Umed", -3000),
        ).toTopDebtors()

        assertEquals(listOf("Vali", "Umed", "Ali"), result.map { it.name })
        assertEquals(-5000L, result.first().balanceInDirams)
    }

    @Test
    fun `ограничение по лимиту`() {
        val rows = (1..10).map { row(it.toLong(), "D$it", -it * 100L) }
        val result = rows.toTopDebtors(limit = 5)

        assertEquals(5, result.size)
        // самый большой долг -1000 (D10) первым
        assertEquals("D10", result.first().name)
        assertTrue(result.all { it.balanceInDirams < 0 })
    }

    @Test
    fun `пустой список при отсутствии должников`() {
        val result = listOf(row(1, "Ali", 200)).toTopDebtors()
        assertTrue(result.isEmpty())
    }
}
