package tj.daftariqarzho.app.feature.debtors.presentation

import tj.daftariqarzho.app.feature.debtors.domain.model.DebtOverview
import tj.daftariqarzho.app.feature.debtors.domain.model.DebtorSummary
import kotlin.math.roundToInt

data class DebtorsUiState(
    val isLoading: Boolean = true,
    val query: String = "",
    val debtors: List<DebtorSummary> = emptyList(),
    val overview: DebtOverview = DebtOverview(),
) {
    val isEmpty: Boolean get() = !isLoading && debtors.isEmpty()

    val returnedPercent: Int get() = (overview.returnedFraction * 100).roundToInt()
}
