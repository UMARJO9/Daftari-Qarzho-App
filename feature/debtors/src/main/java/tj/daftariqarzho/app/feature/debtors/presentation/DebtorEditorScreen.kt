package tj.daftariqarzho.app.feature.debtors.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import tj.daftariqarzho.app.feature.debtors.R
import tj.daftariqarzho.app.feature.debtors.domain.usecase.SaveDebtorUseCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtorEditorScreen(
    debtorId: Long?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DebtorEditorViewModel = koinViewModel(
        parameters = { if (debtorId == null) parametersOf() else parametersOf(debtorId) },
    ),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onBack()
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Text(
                        stringResource(
                            if (state.isEditMode) {
                                R.string.debtor_editor_title_edit
                            } else {
                                R.string.debtor_editor_title_add
                            },
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.debtor_detail_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = DaftarTheme.colors.primary,
                )
            } else {
                DebtorEditorContent(state = state, onEvent = viewModel::onEvent)
            }
        }
    }
}

@Composable
private fun DebtorEditorContent(
    state: DebtorEditorUiState,
    onEvent: (DebtorEditorEvent) -> Unit,
) {
    val nameFocusRequester = remember { FocusRequester() }

    LaunchedEffect(state.isEditMode) {
        if (!state.isEditMode) {
            nameFocusRequester.requestFocus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(
                start = DaftarTheme.spacing.screenPadding,
                end = DaftarTheme.spacing.screenPadding,
                top = DaftarTheme.spacing.md,
                bottom = DaftarTheme.spacing.xl,
            ),
        verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.md),
    ) {
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
