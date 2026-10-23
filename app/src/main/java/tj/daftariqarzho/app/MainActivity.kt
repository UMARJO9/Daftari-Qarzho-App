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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import tj.daftariqarzho.app.core.designsystem.theme.DaftarTheme
import tj.daftariqarzho.app.feature.debtors.DebtorCreateRoute
import tj.daftariqarzho.app.feature.debtors.DebtorDetailRoute
import tj.daftariqarzho.app.feature.debtors.DebtorEditRoute
import tj.daftariqarzho.app.feature.debtors.debtorDetailScreen
import tj.daftariqarzho.app.feature.debtors.debtorEditorScreen

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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AppRoot() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val isHome = currentDestination == null || currentDestination.hasRoute(HomeRoute::class)
    val glassBarVisible = isHome && !WindowInsets.isImeVisible

    Scaffold { innerPadding ->
        DaftarNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = if (glassBarVisible) 0.dp else innerPadding.calculateBottomPadding(),
                ),
        )
    }
}

@Composable
private fun DaftarNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onDebtorClick = { debtorId ->
                    navController.navigate(DebtorDetailRoute(debtorId))
                },
                onAddDebtorClick = { navController.navigate(DebtorCreateRoute) },
            )
        }
        debtorDetailScreen(
            onBack = { navController.popBackStack() },
            onEditClick = { debtorId ->
                navController.navigate(DebtorEditRoute(debtorId))
            },
        )
        debtorEditorScreen(
            onBack = { navController.popBackStack() },
        )
    }
}
