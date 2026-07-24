package tj.daftariqarzho.app.feature.transactions.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import tj.daftariqarzho.app.core.database.entity.TransactionEntity
import tj.daftariqarzho.app.core.database.entity.TransactionWithDebtor

class TransactionMappersTest {

    @Test
    fun `toItem переносит поля операции и имя должника`() {
        val row = TransactionWithDebtor(
            transaction = TransactionEntity(
                id = 5,
                debtorId = 3,
                amountInDirams = -12500,
                comment = "нон",
                dueDate = 999,
                photoUri = null,
                createdAt = 1000,
            ),
            debtorName = "Иброхим",
        )
        val item = row.toItem()
        assertEquals(5L, item.id)
        assertEquals(3L, item.debtorId)
        assertEquals("Иброхим", item.debtorName)
        assertEquals(-12500L, item.amountInDirams)
        assertEquals("нон", item.comment)
        assertEquals(1000L, item.createdAt)
    }
}
