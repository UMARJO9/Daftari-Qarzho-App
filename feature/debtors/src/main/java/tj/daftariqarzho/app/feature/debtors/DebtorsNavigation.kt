package tj.daftariqarzho.app.feature.debtors

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object DebtorsRoute

fun NavGraphBuilder.debtorsScreen() {
    composable<DebtorsRoute> {
        DebtorsScreen()
    }
}
