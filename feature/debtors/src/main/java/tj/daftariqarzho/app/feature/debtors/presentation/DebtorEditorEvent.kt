package tj.daftariqarzho.app.feature.debtors.presentation

sealed interface DebtorEditorEvent {

    data class NameChanged(val value: String) : DebtorEditorEvent

    data class PhoneChanged(val value: String) : DebtorEditorEvent

    data class NoteChanged(val value: String) : DebtorEditorEvent

    data object Save : DebtorEditorEvent
}
