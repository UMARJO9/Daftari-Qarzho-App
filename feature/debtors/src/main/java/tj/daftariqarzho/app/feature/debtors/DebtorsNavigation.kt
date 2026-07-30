package tj.daftariqarzho.app.feature.debtors

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorDetailScreen
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorEditorScreen
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorsScreen

@Serializable
data object DebtorsRoute

@Serializable
data class DebtorDetailRoute(val debtorId: Long)

@Serializable
data object DebtorCreateRoute

@Serializable
data class DebtorEditRoute(val debtorId: Long)

fun NavGraphBuilder.debtorsScreen(
    onDebtorClick: (Long) -> Unit,
    onAddDebtorClick: () -> Unit,
) {
    composable<DebtorsRoute> {
        DebtorsScreen(
            onDebtorClick = onDebtorClick,
            onAddClick = onAddDebtorClick,
        )
    }
}

fun NavGraphBuilder.debtorDetailScreen(
    onBack: () -> Unit,
    onEditClick: (Long) -> Unit,
) {
    composable<DebtorDetailRoute> { entry ->
        val route = entry.toRoute<DebtorDetailRoute>()
        DebtorDetailScreen(
            debtorId = route.debtorId,
            onBack = onBack,
            onEditClick = { onEditClick(route.debtorId) },
        )
    }
}

fun NavGraphBuilder.debtorEditorScreen(
    onBack: () -> Unit,
) {
    composable<DebtorCreateRoute> {
        DebtorEditorScreen(debtorId = null, onBack = onBack)
    }
    composable<DebtorEditRoute> { entry ->
        DebtorEditorScreen(
            debtorId = entry.toRoute<DebtorEditRoute>().debtorId,
            onBack = onBack,
        )
    }
}
