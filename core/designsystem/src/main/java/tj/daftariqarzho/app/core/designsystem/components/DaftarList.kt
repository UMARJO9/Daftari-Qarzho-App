package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import tj.daftariqarzho.app.core.designsystem.icons.DaftarIcons
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

private val RowMinHeight = 52.dp
private val CheckIconSize = 22.dp
private val DividerThickness = 0.5.dp
private const val RowPressedAlpha = 0.06f

@Composable
fun DaftarListSection(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.sm),
    ) {
        if (title != null) {
            Text(
                text = title,
                style = DaftarTheme.typography.bodySmall,
                color = DaftarTheme.colors.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = DaftarTheme.spacing.lg),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(DaftarTheme.shapes.large)
                .background(DaftarTheme.colors.surface)
                .selectableGroup(),
            content = content,
        )
    }
}

@Composable
fun DaftarSelectableRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val background by animateColorAsState(
        targetValue = if (pressed) {
            DaftarTheme.colors.onSurface.copy(alpha = RowPressedAlpha)
        } else {
            Color.Transparent
        },
    )
    val checkAlpha by animateFloatAsState(targetValue = if (selected) 1f else 0f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(background)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = RowMinHeight)
                .padding(horizontal = DaftarTheme.spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = DaftarTheme.typography.bodyLarge,
                color = DaftarTheme.colors.onSurface,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = DaftarIcons.Check,
                contentDescription = null,
                tint = DaftarTheme.colors.primary,
                modifier = Modifier
                    .size(CheckIconSize)
                    .graphicsLayer { alpha = checkAlpha },
            )
        }
        if (showDivider) {
            DaftarListDivider()
        }
    }
}

@Composable
fun DaftarListDivider(modifier: Modifier = Modifier) {
    DaftarDivider(modifier = modifier.padding(start = DaftarTheme.spacing.lg))
}

@Composable
fun DaftarDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(DividerThickness)
            .background(DaftarTheme.controlColors.divider),
    )
}

@PreviewLightDark
@Composable
private fun DaftarListPreview() {
    DaftarTheme {
        Surface(color = DaftarTheme.colors.background) {
            Column(modifier = Modifier.padding(DaftarTheme.spacing.lg)) {
                DaftarListSection(title = "Забон") {
                    DaftarSelectableRow(text = "Тоҷикӣ", selected = true, onClick = {})
                    DaftarSelectableRow(text = "Русский", selected = false, onClick = {}, showDivider = false)
                }
            }
        }
    }
}
