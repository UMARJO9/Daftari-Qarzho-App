package tj.daftariqarzho.app.feature.debtors.presentation

import tj.daftariqarzho.app.feature.debtors.domain.model.TransactionType

data class TransactionEditorUiState(
    val type: TransactionType,
    val amountInput: String = "",
    val comment: String = "",
    val dueDate: Long? = null,
    val showAmountError: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
) {
    val canSave: Boolean get() = !isSaving && amountInput.isNotBlank()
}
