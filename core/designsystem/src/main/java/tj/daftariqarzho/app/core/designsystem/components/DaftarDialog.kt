package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

private val DialogMaxWidth = 400.dp

@Composable
fun DaftarDialog(
    title: String,
    text: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    destructive: Boolean = false,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(max = DialogMaxWidth)
                .fillMaxWidth()
                .clip(DaftarTheme.shapes.large)
                .background(DaftarTheme.colors.surface)
                .padding(DaftarTheme.spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.sm),
        ) {
            Text(
                text = title,
                style = DaftarTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = DaftarTheme.colors.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = text,
                style = DaftarTheme.typography.bodyMedium,
                color = DaftarTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = DaftarTheme.spacing.md),
                horizontalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.md),
            ) {
                DaftarButton(
                    text = dismissText,
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = DaftarButtonDefaults.neutral(),
                )
                DaftarButton(
                    text = confirmText,
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    colors = if (destructive) {
                        DaftarButtonDefaults.solid(DaftarTheme.debtColors.given)
                    } else {
                        DaftarButtonDefaults.primary()
                    },
                )
            }
        }
    }
}
