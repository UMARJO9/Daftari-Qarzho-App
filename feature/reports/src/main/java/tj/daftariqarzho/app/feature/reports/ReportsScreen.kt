package tj.daftariqarzho.app.feature.reports

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import tj.daftariqarzho.app.core.common.datetime.DateFormatter
import tj.daftariqarzho.app.core.common.money.MoneyFormatter
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import tj.daftariqarzho.app.feature.reports.domain.model.ReportDebtor
import tj.daftariqarzho.app.feature.reports.presentation.ReportsUiState
import tj.daftariqarzho.app.feature.reports.presentation.ReportsViewModel

private const val CardBorderAlpha = 0.3f

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ReportsContent(state = state)
}

@Composable
private fun ReportsContent(state: ReportsUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(DaftarTheme.spacing.screenPadding),
        verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.lg),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.xs)) {
            Text(
                text = stringResource(R.string.reports_title),
                style = DaftarTheme.typography.headlineSmall,
                color = DaftarTheme.colors.onBackground,
            )
            Text(
                text = DateFormatter.formatMonthYear(state.monthStartMillis),
                style = DaftarTheme.typography.bodyMedium,
                color = DaftarTheme.colors.onSurfaceVariant,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.md)) {
            StatCard(
                label = stringResource(R.string.reports_given),
                amountInDirams = state.givenInDirams,
                accent = DaftarTheme.debtColors.given,
                modifier = Modifier.weight(1f),
            )
            StatCard(
                label = stringResource(R.string.reports_received),
                amountInDirams = state.receivedInDirams,
                accent = DaftarTheme.debtColors.received,
                modifier = Modifier.weight(1f),
            )
        }

        Text(
            text = stringResource(R.string.reports_top_debtors),
            style = DaftarTheme.typography.titleMedium,
            color = DaftarTheme.colors.onBackground,
        )
        if (state.hasDebtors) {
            state.topDebtors.forEach { debtor ->
                TopDebtorRow(debtor = debtor)
            }
        } else if (!state.isLoading) {
            Text(
                text = stringResource(R.string.reports_no_debtors),
                style = DaftarTheme.typography.bodyMedium,
                color = DaftarTheme.colors.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    amountInDirams: Long,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = DaftarTheme.shapes.large,
        color = DaftarTheme.colors.surface,
        border = BorderStroke(1.dp, DaftarTheme.colors.outline.copy(alpha = CardBorderAlpha)),
    ) {
        Column(
            modifier = Modifier.padding(DaftarTheme.spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DaftarTheme.spacing.sm),
        ) {
            Text(
                text = label,
                style = DaftarTheme.typography.bodySmall,
                color = DaftarTheme.colors.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${MoneyFormatter.formatAmount(amountInDirams)} " +
                    stringResource(R.string.reports_currency_suffix),
                style = DaftarTheme.moneyTypography.medium,
                color = accent,
            )
        }
    }
}

@Composable
private fun TopDebtorRow(debtor: ReportDebtor) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = debtor.name,
            modifier = Modifier.weight(1f),
            style = DaftarTheme.typography.titleMedium,
            color = DaftarTheme.colors.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "${MoneyFormatter.formatAmount(debtor.balanceInDirams)} " +
                stringResource(R.string.reports_currency_suffix),
            style = DaftarTheme.moneyTypography.small,
            color = DaftarTheme.debtColors.given,
        )
    }
}
