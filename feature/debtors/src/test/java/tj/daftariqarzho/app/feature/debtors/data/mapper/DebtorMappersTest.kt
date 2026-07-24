package tj.daftariqarzho.app.feature.debtors.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import tj.daftariqarzho.app.core.database.entity.DebtorEntity
import tj.daftariqarzho.app.core.database.entity.DebtorWithBalance
import tj.daftariqarzho.app.core.database.entity.TransactionEntity
import tj.daftariqarzho.app.feature.debtors.domain.model.Debtor

class DebtorMappersTest {

    private val day = 86_400_000L
    private val entity = DebtorEntity(id = 1, name = "Ali", phone = "992900", note = "vip", createdAt = 1000)

    @Test
    fun `toDomain переносит все поля`() {
        val debtor = entity.toDomain()
        assertEquals(1L, debtor.id)
        assertEquals("Ali", debtor.name)
        assertEquals("992900", debtor.phone)
        assertEquals("vip", debtor.note)
        assertEquals(1000L, debtor.createdAt)
    }

    @Test
    fun `toEntity обратно переносит все поля`() {
        val debtor = Debtor(id = 2, name = "Vali", phone = null, note = null, createdAt = 500)
        val back = debtor.toEntity()
        assertEquals(2L, back.id)
        assertEquals("Vali", back.name)
        assertEquals(null, back.phone)
        assertEquals(null, back.note)
        assertEquals(500L, back.createdAt)
    }

    @Test
    fun `toSummary не помечает просрочку при неотрицательном балансе`() {
        val row = DebtorWithBalance(entity, balance = 5000, nearestDueDate = 100 * day, lastTransactionAt = 100 * day)
        val summary = row.toSummary(nowMillis = 105 * day)
        assertFalse(summary.isOverdue)
        assertEquals(0L, summary.overdueDays)
    }

    @Test
    fun `toSummary помечает просрочку и считает дни при отрицательном балансе`() {
        val row = DebtorWithBalance(entity, balance = -5000, nearestDueDate = 100 * day, lastTransactionAt = 100 * day)
        val summary = row.toSummary(nowMillis = 105 * day)
        assertTrue(summary.isOverdue)
        assertEquals(5L, summary.overdueDays)
        assertEquals(-5000L, summary.balanceInDirams)
    }

    @Test
    fun `toSummary без просрочки если срок в будущем`() {
        val row = DebtorWithBalance(entity, balance = -5000, nearestDueDate = 110 * day, lastTransactionAt = 100 * day)
        val summary = row.toSummary(nowMillis = 105 * day)
        assertFalse(summary.isOverdue)
        assertEquals(0L, summary.overdueDays)
    }

    @Test
    fun `toSummary без просрочки если срок не задан`() {
        val row = DebtorWithBalance(entity, balance = -5000, nearestDueDate = null, lastTransactionAt = 100 * day)
        val summary = row.toSummary(nowMillis = 105 * day)
        assertFalse(summary.isOverdue)
        assertEquals(0L, summary.overdueDays)
    }

    @Test
    fun `transaction toDomain переносит все поля`() {
        val transaction = TransactionEntity(
            id = 7,
            debtorId = 1,
            amountInDirams = -12500,
            comment = "нон",
            dueDate = 200 * day,
            photoUri = "content://photo",
            createdAt = 100 * day,
        )
        val domain = transaction.toDomain()
        assertEquals(7L, domain.id)
        assertEquals(1L, domain.debtorId)
        assertEquals(-12500L, domain.amountInDirams)
        assertEquals("нон", domain.comment)
        assertEquals(200 * day, domain.dueDate)
        assertEquals("content://photo", domain.photoUri)
        assertEquals(100 * day, domain.createdAt)
    }
}
