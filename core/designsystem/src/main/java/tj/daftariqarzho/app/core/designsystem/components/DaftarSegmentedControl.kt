package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

private val SegmentedHeight = 40.dp
private val ThumbInset = 3.dp
private val ThumbElevation = 2.dp

@Composable
fun DaftarSegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val controls = DaftarTheme.controlColors

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(SegmentedHeight)
            .clip(CircleShape)
            .background(controls.segmentTrack)
            .padding(ThumbInset)
            .selectableGroup(),
    ) {
        val segmentWidth = maxWidth / items.size
        val thumbOffset by animateDpAsState(
            targetValue = segmentWidth * selectedIndex.coerceIn(0, items.lastIndex),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
        )
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .width(segmentWidth)
                .fillMaxHeight()
                .shadow(ThumbElevation, CircleShape)
                .background(controls.segmentThumb, CircleShape),
        )
        Row(modifier = Modifier.fillMaxSize()) {
            items.forEachIndexed { index, item ->
                Segment(
                    text = item,
                    selected = index == selectedIndex,
                    onClick = { onSelect(index) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
            }
        }
    }
}

@Composable
private fun Segment(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textColor by animateColorAsState(
        targetValue = if (selected) DaftarTheme.colors.onSurface else DaftarTheme.colors.onSurfaceVariant,
    )
    Box(
        modifier = modifier.selectable(
            selected = selected,
            onClick = onClick,
            role = Role.Tab,
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
        ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DaftarTheme.typography.labelLarge.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            ),
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@PreviewLightDark
@Composable
private fun DaftarSegmentedControlPreview() {
    DaftarTheme {
        Surface(color = DaftarTheme.colors.background) {
            Column(
                modifier = Modifier.padding(DaftarTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.md),
            ) {
                var selected by remember { mutableIntStateOf(0) }
                DaftarSegmentedControl(
                    items = listOf("Ҳама", "Имрӯз", "Ҳафта", "Моҳ"),
                    selectedIndex = selected,
                    onSelect = { selected = it },
                )
            }
        }
    }
}
