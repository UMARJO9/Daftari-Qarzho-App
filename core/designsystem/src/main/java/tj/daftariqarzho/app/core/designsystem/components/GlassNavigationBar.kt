package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

private val GlassBlurRadius = 24.dp
private const val GlassNoiseFactor = 0.05f
private val GlassShadowElevation = 16.dp
private val GlassBorderWidth = 1.dp
private const val PressedScale = 0.88f

@Immutable
data class GlassNavigationItem(
    val icon: ImageVector,
    val label: String,
)

val LocalGlassBarInset = compositionLocalOf { 0.dp }

@Composable
fun glassNavigationBarInset(): Dp =
    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() +
        DaftarTheme.spacing.glassBarHeight +
        DaftarTheme.spacing.md * 2

@Composable
fun GlassNavigationBar(
    items: List<GlassNavigationItem>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    val glass = DaftarTheme.glassColors
    val spacing = DaftarTheme.spacing
    val style = HazeStyle(
        backgroundColor = DaftarTheme.colors.background,
        tint = HazeTint(glass.tint),
        blurRadius = GlassBlurRadius,
        noiseFactor = GlassNoiseFactor,
        fallbackTint = HazeTint(glass.fallback),
    )
    val borderBrush = Brush.verticalGradient(listOf(glass.highlight, glass.edge))

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = spacing.xl, vertical = spacing.md)
            .height(spacing.glassBarHeight)
            .shadow(
                elevation = GlassShadowElevation,
                shape = CircleShape,
                ambientColor = glass.shadow,
                spotColor = glass.shadow,
            )
            .clip(CircleShape)
            .hazeEffect(state = hazeState, style = style)
            .border(GlassBorderWidth, borderBrush, CircleShape)
            .selectableGroup(),
    ) {
        val itemWidth = maxWidth / items.size
        val indicatorOffset by animateDpAsState(
            targetValue = itemWidth * selectedIndex.coerceAtLeast(0),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
        )
        if (selectedIndex in items.indices) {
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(itemWidth)
                    .fillMaxHeight()
                    .padding(spacing.glassIndicatorInset)
                    .clip(CircleShape)
                    .background(glass.indicator)
                    .border(
                        width = GlassBorderWidth,
                        brush = Brush.verticalGradient(listOf(glass.highlight, Color.Transparent)),
                        shape = CircleShape,
                    ),
            )
        }
        Row(modifier = Modifier.fillMaxSize()) {
            items.forEachIndexed { index, item ->
                GlassNavigationBarItem(
                    item = item,
                    selected = index == selectedIndex,
                    onClick = { onItemClick(index) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
            }
        }
    }
}

@Composable
private fun GlassNavigationBarItem(
    item: GlassNavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (pressed) PressedScale else 1f)
    val contentColor by animateColorAsState(
        targetValue = if (selected) DaftarTheme.colors.primary else DaftarTheme.colors.onSurfaceVariant,
    )

    Column(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = contentColor,
        )
        Text(
            text = item.label,
            style = DaftarTheme.typography.labelSmall,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
