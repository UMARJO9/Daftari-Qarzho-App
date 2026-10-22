package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import tj.daftariqarzho.app.core.designsystem.icons.DaftarIcons
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

private val FieldHeight = 52.dp
private val FieldBorderWidth = 1.5.dp
private val FieldIconSize = 20.dp
private val ClearButtonSize = 22.dp
private val ClearIconSize = 14.dp
private const val FieldContainerAlpha = 0.06f
private const val ClearButtonAlpha = 0.12f

@Composable
fun DaftarTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    error: String? = null,
    onClear: (() -> Unit)? = null,
    clearContentDescription: String? = null,
    focusRequester: FocusRequester? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val colors = DaftarTheme.colors
    val spacing = DaftarTheme.spacing
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val borderColor by animateColorAsState(
        targetValue = when {
            error != null -> DaftarTheme.debtColors.given
            focused -> colors.primary
            else -> Color.Transparent
        },
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        if (label != null) {
            Text(
                text = label,
                style = DaftarTheme.typography.bodySmall,
                color = if (error != null) DaftarTheme.debtColors.given else colors.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = spacing.xs),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
            singleLine = singleLine,
            textStyle = DaftarTheme.typography.bodyLarge.copy(color = colors.onSurface),
            cursorBrush = SolidColor(colors.primary),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = FieldHeight)
                        .clip(DaftarTheme.shapes.medium)
                        .background(colors.onSurface.copy(alpha = FieldContainerAlpha))
                        .border(FieldBorderWidth, borderColor, DaftarTheme.shapes.medium)
                        .padding(horizontal = spacing.lg, vertical = spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.md),
                ) {
                    if (leadingIcon != null) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = null,
                            tint = colors.onSurfaceVariant,
                            modifier = Modifier.size(FieldIconSize),
                        )
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                style = DaftarTheme.typography.bodyLarge,
                                color = colors.onSurfaceVariant,
                                maxLines = 1,
                            )
                        }
                        innerTextField()
                    }
                    if (onClear != null && value.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(ClearButtonSize)
                                .clip(CircleShape)
                                .background(colors.onSurface.copy(alpha = ClearButtonAlpha))
                                .clickable(role = Role.Button, onClick = onClear),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = DaftarIcons.Close,
                                contentDescription = clearContentDescription,
                                tint = colors.onSurfaceVariant,
                                modifier = Modifier.size(ClearIconSize),
                            )
                        }
                    }
                }
            },
        )
        AnimatedVisibility(
            visible = error != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Text(
                text = error.orEmpty(),
                style = DaftarTheme.typography.bodySmall,
                color = DaftarTheme.debtColors.given,
                modifier = Modifier.padding(horizontal = spacing.xs),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DaftarTextFieldPreview() {
    DaftarTheme {
        Surface(color = DaftarTheme.colors.background) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DaftarTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.md),
            ) {
                DaftarTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Ҷустуҷӯ",
                    leadingIcon = DaftarIcons.Search,
                )
                DaftarTextField(value = "Алишер", onValueChange = {}, label = "Ном")
                DaftarTextField(value = "", onValueChange = {}, label = "Ном", error = "Номро ворид кунед")
            }
        }
    }
}
