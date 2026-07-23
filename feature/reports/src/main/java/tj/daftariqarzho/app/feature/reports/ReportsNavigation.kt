package tj.daftariqarzho.app.feature.reports

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ReportsRoute

fun NavGraphBuilder.reportsScreen() {
    composable<ReportsRoute> {
        ReportsScreen()
    }
}
