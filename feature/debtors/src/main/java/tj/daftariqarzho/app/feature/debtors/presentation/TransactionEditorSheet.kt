package tj.daftariqarzho.app.feature.debtors.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import tj.daftariqarzho.app.feature.debtors.R
import tj.daftariqarzho.app.feature.debtors.domain.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditorSheet(
    debtorId: Long,
    type: TransactionType,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionEditorViewModel = koinViewModel(
        key = "transaction_editor_${debtorId}_$type",
        parameters = { parametersOf(debtorId, type) },
    ),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        containerColor = DaftarTheme.colors.surface,
    ) {
        TransactionEditorContent(
            state = state,
            onEvent = viewModel::onEvent,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionEditorContent(
    state: TransactionEditorUiState,
    onEvent: (TransactionEditorEvent) -> Unit,
) {
    val accentColor = when (state.type) {
        TransactionType.DEBT -> DaftarTheme.debtColors.given
        TransactionType.PAYMENT -> DaftarTheme.debtColors.received
    }
    val amountFocusRequester = remember { FocusRequester() }
    var datePickerVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        amountFocusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .navigationBarsPadding()
            .padding(
                start = DaftarTheme.spacing.screenPadding,
                end = DaftarTheme.spacing.screenPadding,
                bottom = DaftarTheme.spacing.xl,
            ),
        verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.md),
    ) {
        Text(
            text = stringResource(
                when (state.type) {
                    TransactionType.DEBT -> R.string.transaction_editor_title_debt
                    TransactionType.PAYMENT -> R.string.transaction_editor_title_payment
                },
            ),
            style = DaftarTheme.typography.headlineSmall,
            color = accentColor,
        )

        OutlinedTextField(
            value = state.amountInput,
            onValueChange = { onEvent(TransactionEditorEvent.AmountChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(amountFocusRequester),
            label = { Text(stringResource(R.string.transaction_editor_amount)) },
            isError = state.showAmountError,
            supportingText = if (state.showAmountError) {
                { Text(stringResource(R.string.transaction_editor_amount_error)) }
            } else {
                null
            },
            singleLine = true,
            shape = DaftarTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        OutlinedTextField(
            value = state.comment,
            onValueChange = { onEvent(TransactionEditorEvent.CommentChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.transaction_editor_comment)) },
            singleLine = true,
            shape = DaftarTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
        )

        if (state.type == TransactionType.DEBT) {
            DueDateRow(
                dueDate = state.dueDate,
                onPickClick = { datePickerVisible = true },
                onClear = { onEvent(TransactionEditorEvent.DueDateChanged(null)) },
            )
        }

        Button(
            onClick = { onEvent(TransactionEditorEvent.Save) },
            modifier = Modifier
                .fillMaxWidth()
                .height(DaftarTheme.spacing.minTouchTarget),
            enabled = state.canSave,
            shape = DaftarTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = Color.White,
            ),
        ) {
            Text(
                text = stringResource(R.string.transaction_editor_save),
                style = DaftarTheme.typography.labelLarge,
            )
        }
    }

    if (datePickerVisible) {
        DueDatePickerDialog(
            initialDate = state.dueDate,
            onConfirm = {
                onEvent(TransactionEditorEvent.DueDateChanged(it))
                datePickerVisible = false
            },
            onDismiss = { datePickerVisible = false },
        )
    }
}

@Composable
private fun DueDateRow(
    dueDate: Long?,
    onPickClick: () -> Unit,
    onClear: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.sm),
    ) {
        OutlinedButton(
            onClick = onPickClick,
            modifier = Modifier.weight(1f),
            shape = DaftarTheme.shapes.medium,
        ) {
            Icon(
                Icons.Filled.DateRange,
                contentDescription = null,
                modifier = Modifier.padding(end = DaftarTheme.spacing.sm),
            )
            Text(
                text = dueDate
                    ?.let { DateFormatter.formatDate(it) }
                    ?: stringResource(R.string.transaction_editor_due_date_pick),
                style = DaftarTheme.typography.labelLarge,
            )
        }
        if (dueDate != null) {
            IconButton(onClick = onClear) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = stringResource(R.string.transaction_editor_due_date_clear),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DueDatePickerDialog(
    initialDate: Long?,
    onConfirm: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(datePickerState.selectedDateMillis) }) {
                Text(stringResource(R.string.transaction_editor_dialog_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.transaction_editor_dialog_cancel))
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}
