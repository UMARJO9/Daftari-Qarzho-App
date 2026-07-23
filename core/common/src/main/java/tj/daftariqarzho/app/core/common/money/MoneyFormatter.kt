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
}
