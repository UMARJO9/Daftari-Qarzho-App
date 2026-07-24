package tj.daftariqarzho.app.feature.debtors.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.core.designsystem.components.DebtGivenButton
import tj.daftariqarzho.app.core.designsystem.components.MoneyText
import tj.daftariqarzho.app.core.designsystem.components.PaymentReceivedButton
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import tj.daftariqarzho.app.feature.debtors.R
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtTransaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtorDetailScreen(
    debtorId: Long,
    onBack: () -> Unit,
    onGiveDebt: (Long) -> Unit,
    onReceivePayment: (Long) -> Unit,
    viewModel: DebtorDetailViewModel = koinViewModel(
        key = "debtor_detail_$debtorId",
        parameters = { parametersOf(debtorId) },
    ),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()

    var editorVisible by remember { mutableStateOf(false) }
    var deleteDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isDeleted, state.notFound) {
        if (isDeleted || state.notFound) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.debtor?.name.orEmpty(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                actions = {
                    if (state.debtor != null) {
                        IconButton(onClick = { editorVisible = true }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = stringResource(R.string.debtor_detail_edit),
                            )
                        }
                        IconButton(onClick = { deleteDialogVisible = true }) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.debtor_detail_delete),
                            )
                        }
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
            when {
                state.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = DaftarTheme.colors.primary,
                )

                state.debtor != null -> DebtorDetailContent(
                    state = state,
                    onGiveDebt = { onGiveDebt(debtorId) },
                    onReceivePayment = { onReceivePayment(debtorId) },
                )
            }
        }
    }

    if (editorVisible) {
        DebtorEditorSheet(
            debtorId = debtorId,
            onDismiss = { editorVisible = false },
        )
    }

    if (deleteDialogVisible) {
        DeleteConfirmDialog(
            onConfirm = {
                deleteDialogVisible = false
                viewModel.onEvent(DebtorDetailEvent.Delete)
            },
            onDismiss = { deleteDialogVisible = false },
        )
    }
}

@Composable
private fun DebtorDetailContent(
    state: DebtorDetailUiState,
    onGiveDebt: () -> Unit,
    onReceivePayment: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        BalanceHeader(balanceInDirams = state.balanceInDirams)
        ActionButtons(onGiveDebt = onGiveDebt, onReceivePayment = onReceivePayment)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = DaftarTheme.spacing.md),
            color = DaftarTheme.colors.outline.copy(alpha = DividerAlpha),
        )
        Text(
            text = stringResource(R.string.debtor_detail_history),
            modifier = Modifier.padding(horizontal = DaftarTheme.spacing.screenPadding),
            style = DaftarTheme.typography.titleMedium,
            color = DaftarTheme.colors.onBackground,
        )
        if (state.isEmpty) {
            EmptyHistory()
        } else {
            TransactionsList(transactions = state.transactions)
        }
    }
}

@Composable
private fun BalanceHeader(balanceInDirams: Long) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = DaftarTheme.spacing.screenPadding,
                vertical = DaftarTheme.spacing.lg,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.xs),
    ) {
        Text(
            text = stringResource(R.string.debtor_detail_balance),
            style = DaftarTheme.typography.bodySmall,
            color = DaftarTheme.colors.onSurfaceVariant,
        )
        MoneyText(
            amountInDirams = balanceInDirams,
            style = DaftarTheme.moneyTypography.large,
        )
    }
}

@Composable
private fun ActionButtons(onGiveDebt: () -> Unit, onReceivePayment: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DaftarTheme.spacing.screenPadding),
        horizontalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.md),
    ) {
        DebtGivenButton(
            text = stringResource(R.string.debtor_detail_give_debt),
            onClick = onGiveDebt,
            modifier = Modifier.weight(1f),
        )
        PaymentReceivedButton(
            text = stringResource(R.string.debtor_detail_receive_payment),
            onClick = onReceivePayment,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun EmptyHistory() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(DaftarTheme.spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.debtor_detail_empty),
            style = DaftarTheme.typography.bodyMedium,
            color = DaftarTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun TransactionsList(transactions: List<DebtTransaction>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = DaftarTheme.spacing.screenPadding,
            end = DaftarTheme.spacing.screenPadding,
            top = DaftarTheme.spacing.md,
            bottom = DaftarTheme.spacing.screenPadding,
        ),
        verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.listItemGap),
    ) {
        items(transactions, key = { it.id }) { transaction ->
            TransactionRow(transaction = transaction)
        }
    }
}

@Composable
private fun TransactionRow(transaction: DebtTransaction) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.comment
                    ?.takeIf { it.isNotBlank() }
                    ?: stringResource(R.string.debtor_detail_no_comment),
                style = DaftarTheme.typography.bodyMedium,
                color = DaftarTheme.colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = DateFormatter.formatDate(transaction.createdAt),
                style = DaftarTheme.typography.bodySmall,
                color = DaftarTheme.colors.onSurfaceVariant,
            )
            transaction.dueDate?.let { due ->
                Text(
                    text = stringResource(
                        R.string.debtor_detail_due,
                        DateFormatter.formatDate(due),
                    ),
                    style = DaftarTheme.typography.bodySmall,
                    color = DaftarTheme.colors.onSurfaceVariant,
                )
            }
        }
        MoneyText(amountInDirams = transaction.amountInDirams)
    }
}

@Composable
private fun DeleteConfirmDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.debtor_detail_delete_confirm_title)) },
        text = { Text(stringResource(R.string.debtor_detail_delete_confirm_text)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.debtor_detail_delete_confirm_ok),
                    color = DaftarTheme.debtColors.given,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.debtor_detail_delete_confirm_cancel))
            }
        },
    )
}

private const val DividerAlpha = 0.3f
