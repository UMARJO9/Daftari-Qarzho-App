package tj.daftariqarzho.app.feature.debtors.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.core.designsystem.components.DaftarButton
import tj.daftariqarzho.app.core.designsystem.components.DaftarButtonDefaults
import tj.daftariqarzho.app.core.designsystem.components.DaftarIconButton
import tj.daftariqarzho.app.core.designsystem.components.DaftarTextField
import tj.daftariqarzho.app.core.designsystem.icons.DaftarIcons
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
    val dismiss = {
        viewModel.onEvent(TransactionEditorEvent.Reset)
        onDismiss()
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            dismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = dismiss,
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

        DaftarTextField(
            value = state.amountInput,
            onValueChange = { onEvent(TransactionEditorEvent.AmountChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.transaction_editor_amount),
            error = if (state.showAmountError) {
                stringResource(R.string.transaction_editor_amount_error)
            } else {
                null
            },
            focusRequester = amountFocusRequester,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        DaftarTextField(
            value = state.comment,
            onValueChange = { onEvent(TransactionEditorEvent.CommentChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.transaction_editor_comment),
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

        DaftarButton(
            text = stringResource(R.string.transaction_editor_save),
            onClick = { onEvent(TransactionEditorEvent.Save) },
            modifier = Modifier.fillMaxWidth(),
            colors = DaftarButtonDefaults.solid(accentColor),
            enabled = state.canSave,
        )
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
        DaftarButton(
            text = dueDate
                ?.let { DateFormatter.formatDate(it) }
                ?: stringResource(R.string.transaction_editor_due_date_pick),
            onClick = onPickClick,
            modifier = Modifier.weight(1f),
            colors = DaftarButtonDefaults.neutral(),
            icon = DaftarIcons.Calendar,
        )
        if (dueDate != null) {
            DaftarIconButton(
                icon = DaftarIcons.Close,
                contentDescription = stringResource(R.string.transaction_editor_due_date_clear),
                onClick = onClear,
            )
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
    val todayUtc = remember { DateFormatter.localDateToUtcStartOfDay(System.currentTimeMillis()) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate?.let { DateFormatter.localDateToUtcStartOfDay(it) },
        selectableDates = remember(todayUtc) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= todayUtc
            }
        },
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DaftarButton(
                text = stringResource(R.string.transaction_editor_dialog_ok),
                onClick = {
                    onConfirm(datePickerState.selectedDateMillis?.let { DateFormatter.utcDateToLocalStartOfDay(it) })
                },
                colors = DaftarButtonDefaults.plain(),
            )
        },
        dismissButton = {
            DaftarButton(
                text = stringResource(R.string.transaction_editor_dialog_cancel),
                onClick = onDismiss,
                colors = DaftarButtonDefaults.plain(),
            )
        },
    ) {
        DatePicker(state = datePickerState)
    }
}
