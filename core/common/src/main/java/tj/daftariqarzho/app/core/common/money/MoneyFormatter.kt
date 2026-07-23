package tj.daftariqarzho.app.core.common.money

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

/**
 * Форматтер денежных сумм, хранящихся в дирамах (minor units).
 *
 * 1 сомони = 100 дирамов. Суммы хранятся в [Long], чтобы исключить ошибки
 * округления `float`/`double`. Формат — «1 250,50»: пробел разделяет тысячи,
 * запятая отделяет дирамы. Валюта («сом») здесь не добавляется — это задача
 * UI-слоя (строки через `strings.xml`).
 */
object MoneyFormatter {

    private const val DIRAMS_IN_SOMONI = 100

    /** Форматирует неотрицательную величину: 125050 → «1 250,50». */
    fun formatAmount(amountInDirams: Long): String {
        val value = abs(amountInDirams)
        val symbols = DecimalFormatSymbols(Locale.US).apply { groupingSeparator = ' ' }
        val whole = DecimalFormat("#,##0", symbols).format(value / DIRAMS_IN_SOMONI)
        val fraction = (value % DIRAMS_IN_SOMONI).toInt().toString().padStart(2, '0')
        return "$whole,$fraction"
    }

    /**
     * Форматирует сумму со знаком: положительная → «+…», отрицательная → «−…»
     * (используется типографский минус U+2212).
     */
    fun formatSigned(amountInDirams: Long): String {
        val prefix = if (amountInDirams < 0) "−" else "+"
        return prefix + formatAmount(amountInDirams)
    }
}
