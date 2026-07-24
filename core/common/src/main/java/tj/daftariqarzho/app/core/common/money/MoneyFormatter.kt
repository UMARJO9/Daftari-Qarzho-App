package tj.daftariqarzho.app.core.common.money

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

object MoneyFormatter {

    private const val DIRAMS_IN_SOMONI = 100

    fun formatAmount(amountInDirams: Long): String {
        val value = abs(amountInDirams)
        val symbols = DecimalFormatSymbols(Locale.US).apply { groupingSeparator = ' ' }
        val whole = DecimalFormat("#,##0", symbols).format(value / DIRAMS_IN_SOMONI)
        val fraction = (value % DIRAMS_IN_SOMONI).toInt().toString().padStart(2, '0')
        return "$whole,$fraction"
    }

    fun formatSigned(amountInDirams: Long): String {
        val prefix = if (amountInDirams < 0) "−" else "+"
        return prefix + formatAmount(amountInDirams)
    }

    private val amountPattern = Regex("^\\d+([.,]\\d{0,2})?$")

    fun parseSomoniToDirams(input: String): Long? {
        val normalized = input.trim().replace(" ", "").replace(',', '.')
        if (!amountPattern.matches(normalized)) return null

        val parts = normalized.split('.')
        val whole = parts[0].toLongOrNull() ?: return null
        val fraction = parts.getOrNull(1)?.padEnd(2, '0')?.toLong() ?: 0L
        return try {
            Math.addExact(Math.multiplyExact(whole, DIRAMS_IN_SOMONI.toLong()), fraction)
        } catch (_: ArithmeticException) {
            null
        }
    }

    fun sanitizeAmountInput(input: String): String {
        val filtered = buildString {
            var separatorUsed = false
            var decimals = 0
            for (ch in input) {
                when {
                    ch.isDigit() -> {
                        if (separatorUsed) {
                            if (decimals < 2) {
                                append(ch)
                                decimals++
                            }
                        } else {
                            append(ch)
                        }
                    }

                    (ch == '.' || ch == ',') && !separatorUsed && isNotEmpty() -> {
                        append('.')
                        separatorUsed = true
                    }
                }
            }
        }
        return filtered
    }
}
