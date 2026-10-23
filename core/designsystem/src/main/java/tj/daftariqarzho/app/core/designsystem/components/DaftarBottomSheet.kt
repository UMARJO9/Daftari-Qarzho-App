package tj.daftariqarzho.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme

private val HandleWidth = 36.dp
private val HandleHeight = 5.dp
private const val HandleAlpha = 0.2f
private const val ScrimAlpha = 0.4f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = DaftarTheme.shapes.extraLarge,
        containerColor = DaftarTheme.colors.surface,
        contentColor = DaftarTheme.colors.onSurface,
        tonalElevation = 0.dp,
        scrimColor = DaftarTheme.colors.scrim.copy(alpha = ScrimAlpha),
        dragHandle = { SheetHandle() },
        content = content,
    )
}

@Composable
private fun SheetHandle() {
    Box(
        modifier = Modifier
            .padding(vertical = DaftarTheme.spacing.md)
            .size(width = HandleWidth, height = HandleHeight)
            .clip(CircleShape)
            .background(DaftarTheme.colors.onSurface.copy(alpha = HandleAlpha)),
    )
}
