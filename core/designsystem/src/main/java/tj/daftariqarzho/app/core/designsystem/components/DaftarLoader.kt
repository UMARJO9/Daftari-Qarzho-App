package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val LoaderSize = 32.dp
private const val SpokeCount = 8
private const val RotationMillis = 800
private const val SpokeWidthRatio = 0.09f
private const val SpokeInnerRatio = 0.5f
private const val MinSpokeAlpha = 0.15f

@Composable
fun DaftarLoader(
    modifier: Modifier = Modifier,
    color: Color = DaftarTheme.colors.onSurfaceVariant,
) {
    val step = rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = SpokeCount.toFloat(),
        animationSpec = infiniteRepeatable(tween(RotationMillis, easing = LinearEasing)),
    )

    Canvas(
        modifier = modifier
            .size(LoaderSize)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate },
    ) {
        val head = step.value.toInt() % SpokeCount
        val strokeWidth = size.minDimension * SpokeWidthRatio
        val outerRadius = size.minDimension / 2 - strokeWidth / 2
        val innerRadius = size.minDimension / 2 * SpokeInnerRatio
        repeat(SpokeCount) { index ->
            val age = (head - index + SpokeCount) % SpokeCount
            val fade = 1f - age.toFloat() / SpokeCount * (1f - MinSpokeAlpha)
            val angle = 2 * PI * index / SpokeCount - PI / 2
            val direction = Offset(cos(angle).toFloat(), sin(angle).toFloat())
            drawLine(
                color = color.copy(alpha = color.alpha * fade),
                start = center + direction * innerRadius,
                end = center + direction * outerRadius,
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round,
            )
        }
    }
}
