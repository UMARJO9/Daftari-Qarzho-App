package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

private val AvatarSize = 44.dp

private val CardMinHeight = 72.dp

private const val BorderAlpha = 0.3f

@Composable
fun DebtorCard(
    name: String,
    lastOperation: String,
    balanceInDirams: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    overdueText: String? = null,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = DaftarTheme.shapes.large,
        color = DaftarTheme.colors.surface,
        border = BorderStroke(1.dp, DaftarTheme.colors.outline.copy(alpha = BorderAlpha)),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = CardMinHeight)
                .padding(DaftarTheme.spacing.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(AvatarSize)
                    .clip(CircleShape)
                    .background(DaftarTheme.colors.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = initials(name),
                    color = DaftarTheme.colors.onPrimaryContainer,
                    style = DaftarTheme.typography.titleMedium,
                )
            }

            Spacer(Modifier.width(DaftarTheme.spacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = DaftarTheme.typography.titleMedium,
                    color = DaftarTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = lastOperation,
                    style = DaftarTheme.typography.bodySmall,
                    color = DaftarTheme.colors.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(Modifier.width(DaftarTheme.spacing.md))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.xs),
            ) {
                MoneyText(amountInDirams = balanceInDirams)
                if (overdueText != null) {
                    OverdueBadge(text = overdueText)
                }
            }
        }
    }
}

private fun initials(name: String): String =
    name.trim()
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .take(2)
        .map { it.first().uppercaseChar() }
        .joinToString("")

@PreviewLightDark
@Composable
private fun DebtorCardPreview() {
    DaftarTheme {
        Surface(color = DaftarTheme.colors.background) {
            Column(
                modifier = Modifier.padding(DaftarTheme.spacing.screenPadding),
                verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.listItemGap),
            ) {
                DebtorCard(
                    name = "Иброхим Раҷабов",
                    lastOperation = "Вчера · продукты",
                    balanceInDirams = -125050,
                    onClick = {},
                    overdueText = "Просрочено 5 дней",
                )
                DebtorCard(
                    name = "Малика",
                    lastOperation = "3 дня назад · оплата",
                    balanceInDirams = 43000,
                    onClick = {},
                )
            }
        }
    }
}
