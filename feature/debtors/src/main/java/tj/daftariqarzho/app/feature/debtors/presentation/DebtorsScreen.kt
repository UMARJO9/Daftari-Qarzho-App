package tj.daftariqarzho.app.feature.debtors.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.core.designsystem.components.DebtorCard
import tj.daftariqarzho.app.core.designsystem.components.MoneyText
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import tj.daftariqarzho.app.feature.debtors.R
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorSummary

@Composable
fun DebtorsScreen(
    onDebtorClick: (Long) -> Unit,
    viewModel: DebtorsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var editorVisible by rememberSaveable { mutableStateOf(false) }

    DebtorsContent(
        state = state,
        onEvent = viewModel::onEvent,
        onDebtorClick = onDebtorClick,
        onAddClick = { editorVisible = true },
    )

    if (editorVisible) {
        DebtorEditorSheet(
            debtorId = null,
            onDismiss = { editorVisible = false },
        )
    }
}

@Composable
private fun DebtorsContent(
    state: DebtorsUiState,
    onEvent: (DebtorsEvent) -> Unit,
    onDebtorClick: (Long) -> Unit,
    onAddClick: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = DaftarTheme.colors.primary,
                contentColor = DaftarTheme.colors.onPrimary,
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.debtors_add))
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            TotalBalanceHeader(totalInDirams = state.totalBalanceInDirams)
            SearchField(
                query = state.query,
                onQueryChange = { onEvent(DebtorsEvent.QueryChanged(it)) },
            )
            when {
                state.isLoading -> LoadingBox()
                state.isEmpty -> EmptyBox(hasQuery = state.query.isNotBlank())
                else -> DebtorsList(debtors = state.debtors, onDebtorClick = onDebtorClick)
            }
        }
    }
}

@Composable
private fun TotalBalanceHeader(totalInDirams: Long) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = DaftarTheme.spacing.screenPadding,
                vertical = DaftarTheme.spacing.md,
            ),
    ) {
        Text(
            text = stringResource(R.string.debtors_total_balance),
            style = DaftarTheme.typography.bodySmall,
            color = DaftarTheme.colors.onSurfaceVariant,
        )
        MoneyText(
            amountInDirams = totalInDirams,
            style = DaftarTheme.moneyTypography.large,
        )
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DaftarTheme.spacing.screenPadding),
        placeholder = { Text(stringResource(R.string.debtors_search_hint)) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        singleLine = true,
        shape = DaftarTheme.shapes.medium,
    )
}

@Composable
private fun LoadingBox() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = DaftarTheme.colors.primary)
    }
}

@Composable
private fun EmptyBox(hasQuery: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(DaftarTheme.spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(
                if (hasQuery) R.string.debtors_empty_search else R.string.debtors_empty,
            ),
            style = DaftarTheme.typography.bodyMedium,
            color = DaftarTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun DebtorsList(debtors: List<DebtorSummary>, onDebtorClick: (Long) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(DaftarTheme.spacing.screenPadding),
        verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.listItemGap),
    ) {
        items(debtors, key = { it.debtor.id }) { summary ->
            DebtorCard(
                name = summary.debtor.name,
                lastOperation = summary.lastTransactionAt
                    ?.let { DateFormatter.formatDate(it) }
                    ?: stringResource(R.string.debtors_no_operations),
                balanceInDirams = summary.balanceInDirams,
                onClick = { onDebtorClick(summary.debtor.id) },
                overdueText = if (summary.overdueDays > 0) {
                    stringResource(R.string.debtors_overdue_days, summary.overdueDays)
                } else {
                    null
                },
            )
        }
    }
}
