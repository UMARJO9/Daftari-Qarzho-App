package tj.daftariqarzho.app.feature.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.core.designsystem.components.MoneyText
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionItem
import tj.daftariqarzho.app.feature.transactions.presentation.DayGroup
import tj.daftariqarzho.app.feature.transactions.presentation.TransactionPeriod
import tj.daftariqarzho.app.feature.transactions.presentation.TransactionsEvent
import tj.daftariqarzho.app.feature.transactions.presentation.TransactionsUiState
import tj.daftariqarzho.app.feature.transactions.presentation.TransactionsViewModel

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TransactionsContent(state = state, onEvent = viewModel::onEvent)
}

@Composable
private fun TransactionsContent(
    state: TransactionsUiState,
    onEvent: (TransactionsEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.transactions_title),
            modifier = Modifier.padding(
                start = DaftarTheme.spacing.screenPadding,
                end = DaftarTheme.spacing.screenPadding,
                top = DaftarTheme.spacing.lg,
            ),
            style = DaftarTheme.typography.headlineSmall,
            color = DaftarTheme.colors.onBackground,
        )
        PeriodFilter(
            selected = state.period,
            onSelect = { onEvent(TransactionsEvent.PeriodSelected(it)) },
        )
        when {
            state.isLoading -> Unit
            state.isEmpty -> EmptyState()
            else -> TransactionsList(groups = state.groups)
        }
    }
}

@Composable
private fun PeriodFilter(
    selected: TransactionPeriod,
    onSelect: (TransactionPeriod) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = DaftarTheme.spacing.screenPadding,
                vertical = DaftarTheme.spacing.md,
            ),
        horizontalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.sm),
    ) {
        TransactionPeriod.entries.forEach { period ->
            FilterChip(
                selected = period == selected,
                onClick = { onSelect(period) },
                label = { Text(stringResource(period.labelRes())) },
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(DaftarTheme.spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.transactions_empty),
            style = DaftarTheme.typography.bodyMedium,
            color = DaftarTheme.colors.onSurfaceVariant,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TransactionsList(groups: List<DayGroup>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = DaftarTheme.spacing.screenPadding),
    ) {
        groups.forEach { group ->
            stickyHeader(key = "header_${group.dayMillis}") {
                DayHeader(dayMillis = group.dayMillis, netInDirams = group.netInDirams)
            }
            items(group.items, key = { it.id }) { item ->
                TransactionRow(item = item)
            }
        }
    }
}

@Composable
private fun DayHeader(dayMillis: Long, netInDirams: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DaftarTheme.colors.background)
            .padding(
                horizontal = DaftarTheme.spacing.screenPadding,
                vertical = DaftarTheme.spacing.sm,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = DateFormatter.formatDayMonth(dayMillis),
            modifier = Modifier.weight(1f),
            style = DaftarTheme.typography.labelLarge,
            color = DaftarTheme.colors.onSurfaceVariant,
        )
        MoneyText(
            amountInDirams = netInDirams,
            style = DaftarTheme.moneyTypography.small,
        )
    }
}

@Composable
private fun TransactionRow(item: TransactionItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = DaftarTheme.spacing.screenPadding,
                vertical = DaftarTheme.spacing.md,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.debtorName,
                style = DaftarTheme.typography.titleMedium,
                color = DaftarTheme.colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.comment
                    ?.takeIf { it.isNotBlank() }
                    ?: stringResource(R.string.transactions_no_comment),
                style = DaftarTheme.typography.bodySmall,
                color = DaftarTheme.colors.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        MoneyText(amountInDirams = item.amountInDirams)
    }
}

private fun TransactionPeriod.labelRes(): Int = when (this) {
    TransactionPeriod.ALL -> R.string.transactions_period_all
    TransactionPeriod.TODAY -> R.string.transactions_period_today
    TransactionPeriod.WEEK -> R.string.transactions_period_week
    TransactionPeriod.MONTH -> R.string.transactions_period_month
}
