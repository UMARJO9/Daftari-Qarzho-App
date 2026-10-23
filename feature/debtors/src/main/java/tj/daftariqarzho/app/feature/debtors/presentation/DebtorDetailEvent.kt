package tj.daftariqarzho.app.feature.debtors.presentation

sealed interface DebtorDetailEvent {

    data object Delete : DebtorDetailEvent
}
