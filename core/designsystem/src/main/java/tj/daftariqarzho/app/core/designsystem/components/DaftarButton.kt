package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import tj.daftariqarzho.app.core.designsystem.icons.DaftarIcons
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

private val ButtonHeight = 52.dp
private val ButtonIconSize = 20.dp
private val IconButtonSize = 40.dp
private const val ButtonPressedScale = 0.97f
private const val ButtonPressedAlpha = 0.85f
private const val ButtonDisabledAlpha = 0.4f
private const val TonalAlpha = 0.12f
private const val NeutralAlpha = 0.06f

@Immutable
data class DaftarButtonColors(
    val container: Color,
    val content: Color,
)

object DaftarButtonDefaults {

    @Composable
    fun primary(): DaftarButtonColors = DaftarButtonColors(
        container = DaftarTheme.colors.primary,
        content = DaftarTheme.colors.onPrimary,
    )

    @Composable
    fun tonal(): DaftarButtonColors = DaftarButtonColors(
        container = DaftarTheme.colors.primary.copy(alpha = TonalAlpha),
        content = DaftarTheme.colors.primary,
    )

    @Composable
    fun neutral(): DaftarButtonColors = DaftarButtonColors(
        container = DaftarTheme.colors.onSurface.copy(alpha = NeutralAlpha),
        content = DaftarTheme.colors.onSurface,
    )

    @Composable
    fun plain(): DaftarButtonColors = DaftarButtonColors(
        container = Color.Transparent,
        content = DaftarTheme.colors.primary,
    )

    @Composable
    fun destructivePlain(): DaftarButtonColors = DaftarButtonColors(
        container = Color.Transparent,
        content = DaftarTheme.debtColors.given,
    )

    fun solid(container: Color): DaftarButtonColors = DaftarButtonColors(
        container = container,
        content = Color.White,
    )
}

@Composable
fun DaftarButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: DaftarButtonColors = DaftarButtonDefaults.primary(),
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (pressed) ButtonPressedScale else 1f)
    val alpha by animateFloatAsState(
        targetValue = when {
            !enabled -> ButtonDisabledAlpha
            pressed -> ButtonPressedAlpha
            else -> 1f
        },
    )

    Row(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .heightIn(min = ButtonHeight)
            .clip(DaftarTheme.shapes.medium)
            .background(colors.container)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = DaftarTheme.spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.sm, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.content,
                modifier = Modifier.size(ButtonIconSize),
            )
        }
        Text(
            text = text,
            style = DaftarTheme.typography.labelLarge,
            color = colors.content,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun DaftarIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: DaftarButtonColors = DaftarButtonDefaults.neutral(),
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (pressed) GlassPressedScale else 1f)

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    alpha = if (enabled) 1f else ButtonDisabledAlpha
                }
                .size(IconButtonSize)
                .clip(CircleShape)
                .background(colors.container),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = colors.content,
                modifier = Modifier.size(ButtonIconSize),
            )
        }
    }
}

@Composable
fun DebtGivenButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    DaftarButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        colors = DaftarButtonDefaults.solid(DaftarTheme.debtColors.given),
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
    DaftarButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        colors = DaftarButtonDefaults.solid(DaftarTheme.debtColors.received),
        enabled = enabled,
    )
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
                DaftarButton(text = "Сабт кардан", onClick = {}, modifier = Modifier.fillMaxWidth())
                DaftarButton(
                    text = "Сана",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = DaftarButtonDefaults.neutral(),
                    icon = DaftarIcons.Calendar,
                )
                DaftarButton(
                    text = "Бекор кардан",
                    onClick = {},
                    colors = DaftarButtonDefaults.tonal(),
                )
                DebtGivenButton(text = "Қарз додам", onClick = {}, modifier = Modifier.fillMaxWidth())
                PaymentReceivedButton(text = "Пул гирифтам", onClick = {}, modifier = Modifier.fillMaxWidth())
                Row {
                    DaftarIconButton(icon = DaftarIcons.Back, contentDescription = null, onClick = {})
                    DaftarIconButton(icon = DaftarIcons.Edit, contentDescription = null, onClick = {})
                }
            }
        }
    }
}
