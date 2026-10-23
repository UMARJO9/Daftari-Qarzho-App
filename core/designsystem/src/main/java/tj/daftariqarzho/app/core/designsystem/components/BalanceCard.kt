package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import tj.daftariqarzho.app.core.designsystem.icons.DaftarIcons
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

private val ProgressHeight = 8.dp
private val ChipSize = 28.dp
private val ChipIconSize = 16.dp
private const val ProgressAnimationMillis = 700

@Composable
fun BalanceCard(
    title: String,
    balanceInDirams: Long,
    returnedLabel: String,
    returnedFraction: Float,
    givenLabel: String,
    givenInDirams: Long,
    receivedLabel: String,
    receivedInDirams: Long,
    modifier: Modifier = Modifier,
) {
    val hero = DaftarTheme.heroColors
    val spacing = DaftarTheme.spacing
    val progress by animateFloatAsState(
        targetValue = returnedFraction.coerceIn(0f, 1f),
        animationSpec = tween(ProgressAnimationMillis),
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(DaftarTheme.shapes.large)
            .background(Brush.linearGradient(listOf(hero.gradientStart, hero.gradientEnd)))
            .padding(spacing.xl),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
            Text(
                text = title,
                style = DaftarTheme.typography.bodySmall,
                color = hero.contentMuted,
            )
            MoneyText(
                amountInDirams = balanceInDirams,
                style = DaftarTheme.moneyTypography.large,
                color = hero.content,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ProgressHeight)
                    .clip(CircleShape)
                    .background(hero.track),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .clip(CircleShape)
                        .background(hero.content),
                )
            }
            Text(
                text = returnedLabel,
                style = DaftarTheme.typography.bodySmall,
                color = hero.contentMuted,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(spacing.lg)) {
            BalanceFigure(
                icon = DaftarIcons.ArrowDown,
                label = givenLabel,
                amountInDirams = -givenInDirams,
                modifier = Modifier.weight(1f),
            )
            BalanceFigure(
                icon = DaftarIcons.ArrowUp,
                label = receivedLabel,
                amountInDirams = receivedInDirams,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun BalanceFigure(
    icon: ImageVector,
    label: String,
    amountInDirams: Long,
    modifier: Modifier = Modifier,
) {
    val hero = DaftarTheme.heroColors
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.sm),
    ) {
        Box(
            modifier = Modifier
                .size(ChipSize)
                .clip(CircleShape)
                .background(hero.chip),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = hero.content,
                modifier = Modifier.size(ChipIconSize),
            )
        }
        Column {
            Text(
                text = label,
                style = DaftarTheme.typography.bodySmall,
                color = hero.contentMuted,
                maxLines = 1,
            )
            MoneyText(
                amountInDirams = amountInDirams,
                style = DaftarTheme.moneyTypography.small,
                color = hero.content,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun BalanceCardPreview() {
    DaftarTheme {
        Surface(color = DaftarTheme.colors.background) {
            BalanceCard(
                title = "Тавозуни умумӣ",
                balanceInDirams = -125_000,
                returnedLabel = "Баргашт: 62%",
                returnedFraction = 0.62f,
                givenLabel = "Дода шуд",
                givenInDirams = 330_000,
                receivedLabel = "Гирифта шуд",
                receivedInDirams = 205_000,
                modifier = Modifier.padding(DaftarTheme.spacing.lg),
            )
        }
    }
}
