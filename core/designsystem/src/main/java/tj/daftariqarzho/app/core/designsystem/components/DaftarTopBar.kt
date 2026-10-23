package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import tj.daftariqarzho.app.core.designsystem.icons.DaftarIcons
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

private val TopBarHeight = 64.dp

@Composable
fun DaftarTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    backContentDescription: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(TopBarHeight)
            .background(DaftarTheme.colors.background)
            .padding(horizontal = DaftarTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            DaftarIconButton(
                icon = DaftarIcons.Back,
                contentDescription = backContentDescription,
                onClick = onBack,
            )
            Spacer(Modifier.width(DaftarTheme.spacing.xs))
        } else {
            Spacer(Modifier.width(DaftarTheme.spacing.md))
        }
        Text(
            text = title,
            style = DaftarTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = DaftarTheme.colors.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
    }
}

@PreviewLightDark
@Composable
private fun DaftarTopBarPreview() {
    DaftarTheme {
        DaftarTopBar(
            title = "Алишер",
            onBack = {},
            actions = {
                DaftarIconButton(icon = DaftarIcons.Edit, contentDescription = null, onClick = {})
                DaftarIconButton(icon = DaftarIcons.Delete, contentDescription = null, onClick = {})
            },
        )
    }
}
