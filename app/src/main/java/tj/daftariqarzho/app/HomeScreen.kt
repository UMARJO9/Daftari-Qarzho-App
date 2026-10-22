package tj.daftariqarzho.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import tj.daftariqarzho.app.core.designsystem.components.GlassNavigationBar
import tj.daftariqarzho.app.core.designsystem.components.GlassNavigationItem
import tj.daftariqarzho.app.core.designsystem.components.LocalGlassBarInset
import tj.daftariqarzho.app.core.designsystem.components.glassNavigationBarInset
import tj.daftariqarzho.app.core.designsystem.icons.DaftarIcons
import tj.daftariqarzho.app.feature.debtors.DebtorsTab
import tj.daftariqarzho.app.feature.reports.ReportsTab
import tj.daftariqarzho.app.feature.settings.SettingsTab
import tj.daftariqarzho.app.feature.transactions.TransactionsTab

@Serializable
data object HomeRoute

private enum class HomeTab(
    val labelRes: Int,
    val icon: ImageVector,
) {
    DEBTORS(R.string.tab_debtors, DaftarIcons.Debtors),
    TRANSACTIONS(R.string.tab_transactions, DaftarIcons.Transactions),
    REPORTS(R.string.tab_reports, DaftarIcons.Reports),
    SETTINGS(R.string.tab_settings, DaftarIcons.Settings),
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onDebtorClick: (Long) -> Unit,
    onAddDebtorClick: () -> Unit,
) {
    val tabs = HomeTab.entries
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()
    val hazeState = rememberHazeState()
    val showBottomBar = !WindowInsets.isImeVisible
    val glassBarInset = if (showBottomBar) glassNavigationBarInset() else 0.dp

    BackHandler(enabled = pagerState.currentPage != 0) {
        scope.launch { pagerState.animateScrollToPage(0) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CompositionLocalProvider(LocalGlassBarInset provides glassBarInset) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(hazeState),
                key = { tabs[it] },
            ) { page ->
                when (tabs[page]) {
                    HomeTab.DEBTORS -> DebtorsTab(
                        onDebtorClick = onDebtorClick,
                        onAddDebtorClick = onAddDebtorClick,
                    )
                    HomeTab.TRANSACTIONS -> TransactionsTab()
                    HomeTab.REPORTS -> ReportsTab()
                    HomeTab.SETTINGS -> SettingsTab()
                }
            }
        }
        if (showBottomBar) {
            GlassNavigationBar(
                items = tabs.map { tab ->
                    GlassNavigationItem(icon = tab.icon, label = stringResource(tab.labelRes))
                },
                selectedIndex = pagerState.currentPage,
                selectionPosition = { pagerState.currentPage + pagerState.currentPageOffsetFraction },
                onItemClick = { index -> scope.launch { pagerState.animateScrollToPage(index) } },
                hazeState = hazeState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}
