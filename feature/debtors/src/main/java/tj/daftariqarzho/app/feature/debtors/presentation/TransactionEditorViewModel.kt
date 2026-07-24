package tj.daftariqarzho.app.feature.debtors.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tj.daftariqarzho.app.core.common.money.MoneyFormatter
import tj.daftariqarzho.app.feature.debtors.domain.model.TransactionType
import tj.daftariqarzho.app.feature.debtors.domain.usecase.AddTransactionResult
import tj.daftariqarzho.app.feature.debtors.domain.usecase.AddTransactionUseCase

class TransactionEditorViewModel(
    private val debtorId: Long,
    type: TransactionType,
    private val addTransaction: AddTransactionUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionEditorUiState(type = type))
    val uiState: StateFlow<TransactionEditorUiState> = _uiState.asStateFlow()

    fun onEvent(event: TransactionEditorEvent) {
        when (event) {
            is TransactionEditorEvent.AmountChanged -> _uiState.update {
                it.copy(
                    amountInput = MoneyFormatter.sanitizeAmountInput(event.value),
                    showAmountError = false,
                )
            }

            is TransactionEditorEvent.CommentChanged -> _uiState.update {
                it.copy(comment = event.value)
            }

            is TransactionEditorEvent.DueDateChanged -> _uiState.update {
                it.copy(dueDate = event.value)
            }

            TransactionEditorEvent.Save -> save()
        }
    }

    private fun save() {
        val state = _uiState.value
        if (state.isSaving) return

        val amount = MoneyFormatter.parseSomoniToDirams(state.amountInput)
        if (amount == null || amount <= 0) {
            _uiState.update { it.copy(showAmountError = true) }
            return
        }

        _uiState.update { it.copy(isSaving = true, showAmountError = false) }
        viewModelScope.launch {
            val result = addTransaction(
                debtorId = debtorId,
                type = state.type,
                amountInDirams = amount,
                comment = state.comment,
                dueDate = state.dueDate,
            )
            _uiState.update {
                when (result) {
                    is AddTransactionResult.Success -> it.copy(isSaving = false, isSaved = true)
                    AddTransactionResult.InvalidAmount -> it.copy(
                        isSaving = false,
                        showAmountError = true,
                    )
                }
            }
        }
    }
}
