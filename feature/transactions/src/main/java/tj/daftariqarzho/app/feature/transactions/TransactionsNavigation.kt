package tj.daftariqarzho.app.feature.transactions

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object TransactionsRoute

fun NavGraphBuilder.transactionsScreen() {
    composable<TransactionsRoute> {
        TransactionsScreen()
    }
}
