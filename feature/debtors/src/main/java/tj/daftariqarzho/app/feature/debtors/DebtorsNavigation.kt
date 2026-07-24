package tj.daftariqarzho.app.feature.debtors

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorDetailScreen
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorsScreen

@Serializable
data object DebtorsRoute

@Serializable
data class DebtorDetailRoute(val debtorId: Long)

fun NavGraphBuilder.debtorsScreen(
    onDebtorClick: (Long) -> Unit,
) {
    composable<DebtorsRoute> {
        DebtorsScreen(onDebtorClick = onDebtorClick)
    }
}

fun NavGraphBuilder.debtorDetailScreen(
    onBack: () -> Unit,
) {
    composable<DebtorDetailRoute> { entry ->
        val route = entry.toRoute<DebtorDetailRoute>()
        DebtorDetailScreen(
            debtorId = route.debtorId,
            onBack = onBack,
        )
    }
}
