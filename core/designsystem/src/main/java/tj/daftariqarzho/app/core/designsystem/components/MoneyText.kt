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
import tj.daftariqarzho.app.core.common.money.MoneyFormatter
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

@Composable
fun MoneyText(
    amountInDirams: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = DaftarTheme.moneyTypography.medium,
) {
    val color = if (amountInDirams >= 0) {
        DaftarTheme.debtColors.received
    } else {
        DaftarTheme.debtColors.given
    }

    Text(
        text = "${MoneyFormatter.formatSigned(amountInDirams)} сом",
        modifier = modifier,
        color = color,
        style = style,
    )
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
