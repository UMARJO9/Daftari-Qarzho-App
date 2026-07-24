package tj.daftariqarzho.app.feature.debtors.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import tj.daftariqarzho.app.feature.debtors.R
import tj.daftariqarzho.app.feature.debtors.domain.usecase.SaveDebtorUseCase

private val LoadingBoxHeight = 160.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtorEditorSheet(
    debtorId: Long?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DebtorEditorViewModel = koinViewModel(
        key = "debtor_editor_${debtorId ?: "new"}",
        parameters = { if (debtorId == null) parametersOf() else parametersOf(debtorId) },
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
        DebtorEditorContent(
            state = state,
            onEvent = viewModel::onEvent,
        )
    }
}

@Composable
private fun DebtorEditorContent(
    state: DebtorEditorUiState,
    onEvent: (DebtorEditorEvent) -> Unit,
) {
    val nameFocusRequester = remember { FocusRequester() }

    LaunchedEffect(state.isLoading) {
        if (!state.isLoading && !state.isEditMode) {
            nameFocusRequester.requestFocus()
        }
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
                if (state.isEditMode) {
                    R.string.debtor_editor_title_edit
                } else {
                    R.string.debtor_editor_title_add
                },
            ),
            style = DaftarTheme.typography.headlineSmall,
            color = DaftarTheme.colors.onSurface,
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LoadingBoxHeight),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = DaftarTheme.colors.primary)
            }
            return@Column
        }

        OutlinedTextField(
            value = state.name,
            onValueChange = { onEvent(DebtorEditorEvent.NameChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocusRequester),
            label = { Text(stringResource(R.string.debtor_editor_name)) },
            isError = state.nameError != null,
            supportingText = state.nameError?.let { error ->
                {
                    Text(
                        text = when (error) {
                            DebtorNameError.EMPTY ->
                                stringResource(R.string.debtor_editor_error_name_empty)

                            DebtorNameError.TOO_LONG -> stringResource(
                                R.string.debtor_editor_error_name_too_long,
                                SaveDebtorUseCase.MAX_NAME_LENGTH,
                            )
                        },
                        style = DaftarTheme.typography.bodySmall,
                    )
                }
            },
            singleLine = true,
            shape = DaftarTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
        )

        OutlinedTextField(
            value = state.phone,
            onValueChange = { onEvent(DebtorEditorEvent.PhoneChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.debtor_editor_phone)) },
            singleLine = true,
            shape = DaftarTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
            ),
        )

        OutlinedTextField(
            value = state.note,
            onValueChange = { onEvent(DebtorEditorEvent.NoteChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.debtor_editor_note)) },
            singleLine = true,
            shape = DaftarTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
        )

        Button(
            onClick = { onEvent(DebtorEditorEvent.Save) },
            modifier = Modifier
                .fillMaxWidth()
                .height(DaftarTheme.spacing.minTouchTarget),
            enabled = state.canSave,
            shape = DaftarTheme.shapes.medium,
        ) {
            Text(
                text = stringResource(R.string.debtor_editor_save),
                style = DaftarTheme.typography.labelLarge,
            )
        }
    }
}
