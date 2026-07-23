package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

private val ButtonHeight = 52.dp

@Composable
fun DebtGivenButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    DaftarActionButton(
        text = text,
        onClick = onClick,
        containerColor = DaftarTheme.debtColors.given,
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
fun PaymentReceivedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    DaftarActionButton(
        text = text,
        onClick = onClick,
        containerColor = DaftarTheme.debtColors.received,
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
private fun DaftarActionButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(ButtonHeight),
        enabled = enabled,
        shape = DaftarTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.White,
        ),
    ) {
        Text(text = text, style = DaftarTheme.typography.labelLarge)
    }
}

@PreviewLightDark
@Composable
private fun DaftarButtonPreview() {
    DaftarTheme {
        Surface(color = DaftarTheme.colors.background) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DaftarTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.md),
            ) {
                DebtGivenButton(text = "Қарз додам", onClick = {}, modifier = Modifier.fillMaxWidth())
                PaymentReceivedButton(text = "Пул гирифтам", onClick = {}, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
