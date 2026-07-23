package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

@Composable
fun OverdueBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .clip(DaftarTheme.shapes.small)
            .background(DaftarTheme.debtColors.overdueBg)
            .padding(
                horizontal = DaftarTheme.spacing.sm,
                vertical = DaftarTheme.spacing.xs,
            ),
        color = DaftarTheme.debtColors.overdueText,
        style = DaftarTheme.typography.labelSmall,
    )
}

@PreviewLightDark
@Composable
private fun OverdueBadgePreview() {
    DaftarTheme {
        Surface(color = DaftarTheme.colors.background) {
            OverdueBadge(
                text = "Просрочено 5 дней",
                modifier = Modifier.padding(DaftarTheme.spacing.lg),
            )
        }
    }
}
