package tj.daftariqarzho.app.core.common.money

import org.junit.Assert.assertEquals
import org.junit.Test

/** Тесты форматирования денежных сумм в дирамах. */
class MoneyFormatterTest {

    @Test
    fun `формат суммы с дирамами и разделителем тысяч`() {
        assertEquals("1 250,50", MoneyFormatter.formatAmount(125050))
    }

    @Test
    fun `дирамы дополняются ведущим нулём`() {
        assertEquals("1 250,05", MoneyFormatter.formatAmount(125005))
    }

    @Test
    fun `ноль форматируется как ноль сомони и дирамов`() {
        assertEquals("0,00", MoneyFormatter.formatAmount(0))
    }

    @Test
    fun `крупная сумма группируется по разрядам`() {
        assertEquals("12 345,67", MoneyFormatter.formatAmount(1234567))
    }

    @Test
    fun `отрицательная величина форматируется по модулю`() {
        assertEquals("1 250,50", MoneyFormatter.formatAmount(-125050))
    }

    @Test
    fun `знак плюс у неотрицательной суммы`() {
        assertEquals("+43,00", MoneyFormatter.formatSigned(4300))
    }

    @Test
    fun `типографский минус у отрицательной суммы`() {
        assertEquals("−875,00", MoneyFormatter.formatSigned(-87500))
    }
}
