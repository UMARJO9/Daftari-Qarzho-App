package tj.daftariqarzho.app.feature.debtors

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorsScreen

@Serializable
data object DebtorsRoute

fun NavGraphBuilder.debtorsScreen(
    onDebtorClick: (Long) -> Unit,
    onAddClick: () -> Unit,
) {
    composable<DebtorsRoute> {
        DebtorsScreen(
            onDebtorClick = onDebtorClick,
            onAddClick = onAddClick,
        )
    }
}
