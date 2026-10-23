package tj.daftariqarzho.app.feature.debtors.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class DebtOverviewTest {

    @Test
    fun `баланс равен полученному минус выданному`() {
        val overview = DebtOverview(givenInDirams = 330_000, receivedInDirams = 205_000)

        assertEquals(-125_000L, overview.balanceInDirams)
    }

    @Test
    fun `доля возврата считается от выданного`() {
        val overview = DebtOverview(givenInDirams = 400_000, receivedInDirams = 100_000)

        assertEquals(0.25f, overview.returnedFraction, 0.0001f)
    }

    @Test
    fun `без выданных долгов доля возврата нулевая`() {
        val overview = DebtOverview(givenInDirams = 0, receivedInDirams = 50_000)

        assertEquals(0f, overview.returnedFraction, 0.0001f)
    }

    @Test
    fun `переплата не даёт долю больше единицы`() {
        val overview = DebtOverview(givenInDirams = 100_000, receivedInDirams = 150_000)

        assertEquals(1f, overview.returnedFraction, 0.0001f)
        assertEquals(50_000L, overview.balanceInDirams)
    }
}
