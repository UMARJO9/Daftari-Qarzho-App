package tj.daftariqarzho.app.feature.debtors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorDetailScreen
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorEditorScreen
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorsScreen

@Serializable
data class DebtorDetailRoute(val debtorId: Long)

@Serializable
data object DebtorCreateRoute

@Serializable
data class DebtorEditRoute(val debtorId: Long)

@Composable
fun DebtorsTab(
    onDebtorClick: (Long) -> Unit,
    onAddDebtorClick: () -> Unit,
) {
    DebtorsScreen(
        onDebtorClick = dropUnlessResumedWithArgument(onDebtorClick),
        onAddClick = dropUnlessResumed(block = onAddDebtorClick),
    )
}

fun NavGraphBuilder.debtorDetailScreen(
    onBack: () -> Unit,
    onEditClick: (Long) -> Unit,
) {
    composable<DebtorDetailRoute> { entry ->
        val route = entry.toRoute<DebtorDetailRoute>()
        DebtorDetailScreen(
            debtorId = route.debtorId,
            onBack = dropUnlessResumed(block = onBack),
            onEditClick = dropUnlessResumed { onEditClick(route.debtorId) },
        )
    }
}

fun NavGraphBuilder.debtorEditorScreen(
    onBack: () -> Unit,
) {
    composable<DebtorCreateRoute> {
        DebtorEditorScreen(debtorId = null, onBack = dropUnlessResumed(block = onBack))
    }
    composable<DebtorEditRoute> { entry ->
        DebtorEditorScreen(
            debtorId = entry.toRoute<DebtorEditRoute>().debtorId,
            onBack = dropUnlessResumed(block = onBack),
        )
    }
}

@Composable
private fun <T> dropUnlessResumedWithArgument(action: (T) -> Unit): (T) -> Unit {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    return remember(lifecycle, action) {
        { value ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                action(value)
            }
        }
    }
}
