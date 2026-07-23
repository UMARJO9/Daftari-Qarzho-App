package tj.daftariqarzho.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import tj.daftariqarzho.app.feature.debtors.DebtorsRoute
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
            DaftarTheme {
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
    DEBTORS(DebtorsRoute, R.string.tab_debtors, Icons.Filled.Person),
    TRANSACTIONS(TransactionsRoute, R.string.tab_transactions, Icons.AutoMirrored.Filled.List),
    REPORTS(ReportsRoute, R.string.tab_reports, Icons.Filled.DateRange),
    SETTINGS(SettingsRoute, R.string.tab_settings, Icons.Filled.Settings),
}

@Composable
private fun AppRoot() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { DaftarBottomBar(navController) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DebtorsRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            debtorsScreen(onDebtorClick = {}, onAddClick = {})
            transactionsScreen()
            reportsScreen()
            settingsScreen()
        }
    }
}

@Composable
private fun DaftarBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
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
