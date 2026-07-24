package tj.daftariqarzho.app.feature.debtors.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tj.daftariqarzho.app.feature.debtors.domain.usecase.DeleteDebtorUseCase
import tj.daftariqarzho.app.feature.debtors.domain.usecase.ObserveDebtorDetailUseCase

class DebtorDetailViewModel(
    private val debtorId: Long,
    observeDebtorDetail: ObserveDebtorDetailUseCase,
    private val deleteDebtor: DeleteDebtorUseCase,
) : ViewModel() {

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted.asStateFlow()

    val uiState: StateFlow<DebtorDetailUiState> = observeDebtorDetail(debtorId)
        .map { detail ->
            if (detail == null) {
                DebtorDetailUiState(isLoading = false, notFound = true)
            } else {
                DebtorDetailUiState(
                    isLoading = false,
                    debtor = detail.debtor,
                    balanceInDirams = detail.balanceInDirams,
                    transactions = detail.transactions,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DebtorDetailUiState(),
        )

    fun onEvent(event: DebtorDetailEvent) {
        when (event) {
            DebtorDetailEvent.Delete -> delete()
        }
    }

    private fun delete() {
        viewModelScope.launch {
            deleteDebtor(debtorId)
            _isDeleted.value = true
        }
    }
}
