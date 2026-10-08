package tj.daftariqarzho.app.core.common.money

import org.junit.Assert.assertEquals
import org.junit.Test

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

    @Test
    fun `ноль форматируется без знака`() {
        assertEquals("0,00", MoneyFormatter.formatSigned(0))
    }

    @Test
    fun `парсинг целого числа сомони`() {
        assertEquals(12500L, MoneyFormatter.parseSomoniToDirams("125"))
    }

    @Test
    fun `парсинг с точкой и двумя знаками`() {
        assertEquals(12550L, MoneyFormatter.parseSomoniToDirams("125.50"))
    }

    @Test
    fun `парсинг с запятой и одним знаком`() {
        assertEquals(12505L, MoneyFormatter.parseSomoniToDirams("125,05"))
    }

    @Test
    fun `парсинг одного знака после разделителя дополняется нулём`() {
        assertEquals(12550L, MoneyFormatter.parseSomoniToDirams("125.5"))
    }

    @Test
    fun `парсинг игнорирует пробелы`() {
        assertEquals(100000L, MoneyFormatter.parseSomoniToDirams(" 1 000 "))
    }

    @Test
    fun `парсинг пустой строки возвращает null`() {
        assertEquals(null, MoneyFormatter.parseSomoniToDirams(""))
    }

    @Test
    fun `парсинг нечисловой строки возвращает null`() {
        assertEquals(null, MoneyFormatter.parseSomoniToDirams("abc"))
    }

    @Test
    fun `парсинг трёх знаков после разделителя возвращает null`() {
        assertEquals(null, MoneyFormatter.parseSomoniToDirams("125.555"))
    }

    @Test
    fun `фильтр оставляет только цифры и один разделитель`() {
        assertEquals("12.50", MoneyFormatter.sanitizeAmountInput("1a2.5b0.9"))
    }

    @Test
    fun `ведущий разделитель дополняется нулём`() {
        assertEquals("0.50", MoneyFormatter.sanitizeAmountInput(".50"))
        assertEquals("0.5", MoneyFormatter.sanitizeAmountInput(",5"))
    }

    @Test
    fun `фильтр ограничивает целую часть девятью цифрами`() {
        assertEquals("123456789.12", MoneyFormatter.sanitizeAmountInput("1234567890.12"))
    }

    @Test
    fun `парсинг суммы выше лимита возвращает null`() {
        assertEquals(MoneyFormatter.MAX_AMOUNT_IN_DIRAMS, MoneyFormatter.parseSomoniToDirams("999999999.99"))
        assertEquals(null, MoneyFormatter.parseSomoniToDirams("1000000000"))
    }

    @Test
    fun `форматирование минимального Long не ломается`() {
        assertEquals("92 233 720 368 547 758,08", MoneyFormatter.formatAmount(Long.MIN_VALUE))
    }

    @Test
    fun `фильтр ограничивает двумя знаками после разделителя`() {
        assertEquals("12.34", MoneyFormatter.sanitizeAmountInput("12.3456"))
    }
}
