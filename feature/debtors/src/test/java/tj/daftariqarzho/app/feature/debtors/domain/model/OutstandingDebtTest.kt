package tj.daftariqarzho.app.feature.debtors.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OutstandingDebtTest {

    private var nextId = 1L

    private fun debt(amount: Long, createdAt: Long, dueDate: Long? = null) =
        DebtTransaction(nextId++, 1, -amount, null, dueDate, null, createdAt)

    private fun payment(amount: Long, createdAt: Long) =
        DebtTransaction(nextId++, 1, amount, null, null, null, createdAt)

    @Test
    fun `погашенный долг не влияет на срок нового`() {
        val transactions = listOf(
            debt(100, createdAt = 1, dueDate = 10),
            payment(100, createdAt = 5),
            debt(50, createdAt = 31, dueDate = 59),
        )

        assertEquals(59L, transactions.nearestUnpaidDueDate())
    }

    @Test
    fun `частичная оплата оставляет срок самого раннего долга`() {
        val transactions = listOf(
            debt(100, createdAt = 1, dueDate = 10),
            debt(100, createdAt = 2, dueDate = 20),
            payment(150, createdAt = 3),
        )

        assertEquals(20L, transactions.nearestUnpaidDueDate())
    }

    @Test
    fun `оплата идёт на самый старый долг`() {
        val transactions = listOf(
            debt(100, createdAt = 1, dueDate = 30),
            debt(100, createdAt = 2, dueDate = 10),
            payment(50, createdAt = 3),
        )

        assertEquals(10L, transactions.nearestUnpaidDueDate())
    }

    @Test
    fun `переплата гасит следующий долг`() {
        val transactions = listOf(
            payment(100, createdAt = 1),
            debt(80, createdAt = 2, dueDate = 10),
        )

        assertNull(transactions.nearestUnpaidDueDate())
    }

    @Test
    fun `порядок списка не важен`() {
        val transactions = listOf(
            debt(50, createdAt = 31, dueDate = 59),
            payment(100, createdAt = 5),
            debt(100, createdAt = 1, dueDate = 10),
        )

        assertEquals(59L, transactions.nearestUnpaidDueDate())
    }

    @Test
    fun `без сроков возвращает null`() {
        assertNull(listOf(debt(100, createdAt = 1)).nearestUnpaidDueDate())
        assertNull(emptyList<DebtTransaction>().nearestUnpaidDueDate())
    }
}
