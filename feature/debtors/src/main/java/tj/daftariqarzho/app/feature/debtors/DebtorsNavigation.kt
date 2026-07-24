package tj.daftariqarzho.app.feature.debtors

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import tj.daftariqarzho.app.feature.debtors.presentation.DebtorsScreen

@Serializable
data object DebtorsRoute

fun NavGraphBuilder.debtorsScreen() {
    composable<DebtorsRoute> {
        DebtorsScreen()
    }
}
