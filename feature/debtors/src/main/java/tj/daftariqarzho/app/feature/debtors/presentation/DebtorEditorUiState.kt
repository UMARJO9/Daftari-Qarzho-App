package tj.daftariqarzho.app.feature.debtors.presentation

enum class DebtorNameError {
    EMPTY,
    TOO_LONG,
}

data class DebtorEditorUiState(
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val name: String = "",
    val phone: String = "",
    val note: String = "",
    val nameError: DebtorNameError? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
) {
    val canSave: Boolean get() = !isLoading && !isSaving && name.isNotBlank()
}
