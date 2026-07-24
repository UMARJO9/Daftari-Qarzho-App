package tj.daftariqarzho.app.feature.debtors.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tj.daftariqarzho.app.feature.debtors.domain.usecase.GetDebtorUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.SaveDebtorResult
import tj.daftariqarzho.app.feature.debtors.domain.usecase.SaveDebtorUseCase

class DebtorEditorViewModel(
    private val debtorId: Long?,
    private val getDebtor: GetDebtorUseCase,
    private val saveDebtor: SaveDebtorUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DebtorEditorUiState(
            isEditMode = debtorId != null,
            isLoading = debtorId != null,
        ),
    )
    val uiState: StateFlow<DebtorEditorUiState> = _uiState.asStateFlow()

    init {
        if (debtorId != null) {
            loadDebtor(debtorId)
        }
    }

    fun onEvent(event: DebtorEditorEvent) {
        when (event) {
            is DebtorEditorEvent.NameChanged -> _uiState.update {
                it.copy(name = event.value, nameError = null)
            }

            is DebtorEditorEvent.PhoneChanged -> _uiState.update {
                it.copy(phone = event.value)
            }

            is DebtorEditorEvent.NoteChanged -> _uiState.update {
                it.copy(note = event.value)
            }

            DebtorEditorEvent.Save -> save()
        }
    }

    private fun loadDebtor(id: Long) {
        viewModelScope.launch {
            val debtor = getDebtor(id)
            _uiState.update { state ->
                if (debtor == null) {
                    state.copy(isLoading = false, isSaved = true)
                } else {
                    state.copy(
                        isLoading = false,
                        name = debtor.name,
                        phone = debtor.phone.orEmpty(),
                        note = debtor.note.orEmpty(),
                    )
                }
            }
        }
    }

    private fun save() {
        val state = _uiState.value
        if (state.isSaving || state.isLoading) return

        _uiState.update { it.copy(isSaving = true, nameError = null) }
        viewModelScope.launch {
            val result = saveDebtor(
                id = debtorId,
                name = state.name,
                phone = state.phone,
                note = state.note,
            )
            _uiState.update {
                when (result) {
                    is SaveDebtorResult.Success -> it.copy(isSaving = false, isSaved = true)
                    SaveDebtorResult.EmptyName -> it.copy(
                        isSaving = false,
                        nameError = DebtorNameError.EMPTY,
                    )

                    SaveDebtorResult.NameTooLong -> it.copy(
                        isSaving = false,
                        nameError = DebtorNameError.TOO_LONG,
                    )

                    SaveDebtorResult.NotFound -> it.copy(isSaving = false, isSaved = true)
                }
            }
        }
    }
}
