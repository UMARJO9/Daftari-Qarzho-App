package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

internal val GlassBorderWidth = 1.dp
internal const val GlassPressedScale = 0.88f

private val GlassBlurRadius = 24.dp
private const val GlassNoiseFactor = 0.05f
private val GlassShadowElevation = 16.dp

@Composable
fun Modifier.glassBackground(hazeState: HazeState, shape: Shape): Modifier {
    val glass = DaftarTheme.glassColors
    val style = HazeStyle(
        backgroundColor = DaftarTheme.colors.background,
        tint = HazeTint(glass.tint),
        blurRadius = GlassBlurRadius,
        noiseFactor = GlassNoiseFactor,
        fallbackTint = HazeTint(glass.fallback),
    )
    return this
        .shadow(
            elevation = GlassShadowElevation,
            shape = shape,
            ambientColor = glass.shadow,
            spotColor = glass.shadow,
        )
        .clip(shape)
        .hazeEffect(state = hazeState, style = style)
        .border(
            width = GlassBorderWidth,
            brush = Brush.verticalGradient(listOf(glass.highlight, glass.edge)),
            shape = shape,
        )
}
