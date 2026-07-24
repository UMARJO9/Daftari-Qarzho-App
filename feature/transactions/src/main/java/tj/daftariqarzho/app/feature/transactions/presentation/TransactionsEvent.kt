package tj.daftariqarzho.app.feature.transactions.presentation

sealed interface TransactionsEvent {

    data class PeriodSelected(val period: TransactionPeriod) : TransactionsEvent
}
