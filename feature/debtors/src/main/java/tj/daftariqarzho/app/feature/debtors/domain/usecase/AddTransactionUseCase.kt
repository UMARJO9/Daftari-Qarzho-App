package tj.daftariqarzho.app.feature.debtors.domain.usecase

import tj.daftariqarzho.app.feature.debtors.domain.model.DebtTransaction
import tj.daftariqarzho.app.feature.debtors.domain.model.TransactionType
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository

sealed interface AddTransactionResult {

    data class Success(val transactionId: Long) : AddTransactionResult

    data object InvalidAmount : AddTransactionResult
}

class AddTransactionUseCase(
    private val repository: DebtorRepository,
    private val now: () -> Long = System::currentTimeMillis,
) {

    suspend operator fun invoke(
        debtorId: Long,
        type: TransactionType,
        amountInDirams: Long,
        comment: String?,
        dueDate: Long?,
    ): AddTransactionResult {
        if (amountInDirams <= 0) return AddTransactionResult.InvalidAmount

        val signedAmount = when (type) {
            TransactionType.DEBT -> -amountInDirams
            TransactionType.PAYMENT -> amountInDirams
        }
        val trimmedComment = comment?.trim()?.takeIf { it.isNotEmpty() }

        val id = repository.addTransaction(
            DebtTransaction(
                id = 0,
                debtorId = debtorId,
                amountInDirams = signedAmount,
                comment = trimmedComment,
                dueDate = dueDate,
                photoUri = null,
                createdAt = now(),
            ),
        )
        return AddTransactionResult.Success(id)
    }
}
