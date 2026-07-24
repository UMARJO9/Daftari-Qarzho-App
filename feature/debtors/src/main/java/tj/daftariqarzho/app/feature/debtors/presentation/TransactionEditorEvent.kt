package tj.daftariqarzho.app.feature.debtors.presentation

sealed interface TransactionEditorEvent {

    data class AmountChanged(val value: String) : TransactionEditorEvent

    data class CommentChanged(val value: String) : TransactionEditorEvent

    data class DueDateChanged(val value: Long?) : TransactionEditorEvent

    data object Save : TransactionEditorEvent
}
