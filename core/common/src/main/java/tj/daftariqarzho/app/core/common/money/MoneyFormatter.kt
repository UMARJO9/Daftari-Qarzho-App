package tj.daftariqarzho.app.core.common.money

import kotlin.math.abs

object MoneyFormatter {

    private const val DIRAMS_IN_SOMONI = 100
    private const val GROUP_SIZE = 3
    private const val GROUP_SEPARATOR = ' '
    private const val MAX_WHOLE_DIGITS = 9

    const val MAX_AMOUNT_IN_DIRAMS = 99_999_999_999L

    fun formatAmount(amountInDirams: Long): String {
        val whole = groupDigits(abs(amountInDirams / DIRAMS_IN_SOMONI))
        val fraction = abs(amountInDirams % DIRAMS_IN_SOMONI).toInt().toString().padStart(2, '0')
        return "$whole,$fraction"
    }

    private fun groupDigits(value: Long): String {
        val digits = value.toString()
        if (digits.length <= GROUP_SIZE) return digits

        val builder = StringBuilder(digits.length + digits.length / GROUP_SIZE)
        val head = digits.length % GROUP_SIZE
        if (head > 0) builder.append(digits, 0, head)

        var index = head
        while (index < digits.length) {
            if (builder.isNotEmpty()) builder.append(GROUP_SEPARATOR)
            builder.append(digits, index, index + GROUP_SIZE)
            index += GROUP_SIZE
        }
        return builder.toString()
    }

    fun formatSigned(amountInDirams: Long): String {
        val prefix = when {
            amountInDirams < 0 -> "−"
            amountInDirams > 0 -> "+"
            else -> ""
        }
        return prefix + formatAmount(amountInDirams)
    }

    private val amountPattern = Regex("^\\d+([.,]\\d{0,2})?$")

    fun parseSomoniToDirams(input: String): Long? {
        val normalized = input.trim().replace(" ", "").replace(',', '.')
        if (!amountPattern.matches(normalized)) return null

        val parts = normalized.split('.')
        val whole = parts[0].toLongOrNull() ?: return null
        val fraction = parts.getOrNull(1)?.padEnd(2, '0')?.toLong() ?: 0L
        val amount = try {
            Math.addExact(Math.multiplyExact(whole, DIRAMS_IN_SOMONI.toLong()), fraction)
        } catch (_: ArithmeticException) {
            return null
        }
        return amount.takeIf { it <= MAX_AMOUNT_IN_DIRAMS }
    }

    fun sanitizeAmountInput(input: String): String {
        val filtered = buildString {
            var separatorUsed = false
            var decimals = 0
            var wholeDigits = 0
            for (ch in input) {
                when {
                    ch.isDigit() -> {
                        if (separatorUsed) {
                            if (decimals < 2) {
                                append(ch)
                                decimals++
                            }
                        } else if (wholeDigits < MAX_WHOLE_DIGITS) {
                            append(ch)
                            wholeDigits++
                        }
                    }

                    (ch == '.' || ch == ',') && !separatorUsed -> {
                        if (isEmpty()) append('0')
                        append('.')
                        separatorUsed = true
                    }
                }
            }
        }
        return filtered
    }
}
