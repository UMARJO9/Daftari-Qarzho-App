package tj.daftariqarzho.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                DaftarBottomBar(
                    navController = navController,
                    currentDestination = currentDestination,
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DebtorsRoute,
            modifier = Modifier.padding(innerPadding),
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
}

@Composable
private fun DaftarBottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
) {
    NavigationBar {
        TopLevelDestination.entries.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any {
                it.hasRoute(destination.route::class)
            } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(destination.icon, contentDescription = null) },
                label = { Text(stringResource(destination.labelRes)) },
            )
        }
    }
}
