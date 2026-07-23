package tj.daftariqarzho.app.feature.debtors.presentation

sealed interface DebtorsEvent {
    data class QueryChanged(val query: String) : DebtorsEvent
}
