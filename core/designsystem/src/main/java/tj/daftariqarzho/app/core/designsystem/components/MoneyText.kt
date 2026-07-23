package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

@Composable
fun MoneyText(
    amountInDirams: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = DaftarTheme.moneyTypography.medium,
) {
    val positive = amountInDirams >= 0
    val color = if (positive) DaftarTheme.debtColors.received else DaftarTheme.debtColors.given
    val prefix = if (positive) "+" else "−"
    val amount = formatDirams(abs(amountInDirams))

    Text(
        text = "$prefix$amount сом",
        modifier = modifier,
        color = color,
        style = style,
    )
}

private fun formatDirams(dirams: Long): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply { groupingSeparator = ' ' }
    val whole = DecimalFormat("#,##0", symbols).format(dirams / 100)
    val fraction = (dirams % 100).toInt().toString().padStart(2, '0')
    return "$whole,$fraction"
}

@PreviewLightDark
@Composable
private fun MoneyTextPreview() {
    DaftarTheme {
        Surface(color = DaftarTheme.colors.background) {
            Column(
                modifier = Modifier.padding(DaftarTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.sm),
            ) {
                MoneyText(amountInDirams = 125050)
                MoneyText(amountInDirams = -87500)
                MoneyText(amountInDirams = 1234567, style = DaftarTheme.moneyTypography.large)
            }
        }
    }
}
