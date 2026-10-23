package tj.daftariqarzho.app.feature.transactions.presentation

import tj.daftariqarzho.app.feature.transactions.domain.model.TransactionPeriod

sealed interface TransactionsEvent {

    data class PeriodSelected(val period: TransactionPeriod) : TransactionsEvent
}
