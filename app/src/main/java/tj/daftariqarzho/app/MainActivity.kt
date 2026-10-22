package tj.daftariqarzho.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import tj.daftariqarzho.app.core.designsystem.components.GlassNavigationBar
import tj.daftariqarzho.app.core.designsystem.components.GlassNavigationItem
import tj.daftariqarzho.app.core.designsystem.components.LocalGlassBarInset
import tj.daftariqarzho.app.core.designsystem.components.glassNavigationBarInset
import tj.daftariqarzho.app.core.designsystem.icons.DaftarIcons
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import tj.daftariqarzho.app.feature.debtors.DebtorCreateRoute
import tj.daftariqarzho.app.feature.debtors.DebtorDetailRoute
import tj.daftariqarzho.app.feature.debtors.DebtorEditRoute
import tj.daftariqarzho.app.feature.debtors.DebtorsRoute
import tj.daftariqarzho.app.feature.debtors.debtorDetailScreen
import tj.daftariqarzho.app.feature.debtors.debtorEditorScreen
import tj.daftariqarzho.app.feature.debtors.debtorsScreen
import tj.daftariqarzho.app.feature.reports.ReportsRoute
import tj.daftariqarzho.app.feature.reports.reportsScreen
import tj.daftariqarzho.app.feature.settings.SettingsRoute
import tj.daftariqarzho.app.feature.settings.settingsScreen
import tj.daftariqarzho.app.feature.transactions.TransactionsRoute
import tj.daftariqarzho.app.feature.transactions.transactionsScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaftarTheme(darkTheme = isSystemInDarkTheme()) {
                AppRoot()
            }
        }
    }
}

private enum class TopLevelDestination(
    val route: Any,
    val labelRes: Int,
    val icon: ImageVector,
) {
    DEBTORS(DebtorsRoute, R.string.tab_debtors, DaftarIcons.Debtors),
    TRANSACTIONS(TransactionsRoute, R.string.tab_transactions, DaftarIcons.Transactions),
    REPORTS(ReportsRoute, R.string.tab_reports, DaftarIcons.Reports),
    SETTINGS(SettingsRoute, R.string.tab_settings, DaftarIcons.Settings),
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AppRoot() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val isTopLevel = currentDestination == null || currentDestination.hierarchy.any { destination ->
        TopLevelDestination.entries.any { destination.hasRoute(it.route::class) }
    }
    val showBottomBar = isTopLevel && !WindowInsets.isImeVisible
    val hazeState = rememberHazeState()
    val glassBarInset = if (showBottomBar) glassNavigationBarInset() else 0.dp

    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            CompositionLocalProvider(LocalGlassBarInset provides glassBarInset) {
                DaftarNavHost(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .hazeSource(hazeState)
                        .padding(
                            top = innerPadding.calculateTopPadding(),
                            bottom = if (showBottomBar) 0.dp else innerPadding.calculateBottomPadding(),
                        ),
                )
            }
            if (showBottomBar) {
                DaftarBottomBar(
                    navController = navController,
                    currentDestination = currentDestination,
                    hazeState = hazeState,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }
    }
}

@Composable
private fun DaftarNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = DebtorsRoute,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        debtorsScreen(
            onDebtorClick = { debtorId ->
                navController.navigate(DebtorDetailRoute(debtorId))
            },
            onAddDebtorClick = { navController.navigate(DebtorCreateRoute) },
        )
        debtorDetailScreen(
            onBack = { navController.popBackStack() },
            onEditClick = { debtorId ->
                navController.navigate(DebtorEditRoute(debtorId))
            },
        )
        debtorEditorScreen(
            onBack = { navController.popBackStack() },
        )
        transactionsScreen()
        reportsScreen()
        settingsScreen()
    }
}

@Composable
private fun DaftarBottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    val destinations = TopLevelDestination.entries
    val selectedIndex = destinations.indexOfFirst { destination ->
        currentDestination?.hierarchy?.any { it.hasRoute(destination.route::class) } == true
    }
    GlassNavigationBar(
        items = destinations.map { destination ->
            GlassNavigationItem(
                icon = destination.icon,
                label = stringResource(destination.labelRes),
            )
        },
        selectedIndex = selectedIndex,
        onItemClick = { index ->
            navController.navigate(destinations[index].route) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        hazeState = hazeState,
        modifier = modifier,
    )
}
