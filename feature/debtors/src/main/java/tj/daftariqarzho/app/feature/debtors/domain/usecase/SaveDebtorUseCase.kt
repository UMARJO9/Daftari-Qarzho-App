package tj.daftariqarzho.app.feature.debtors.domain.usecase

import tj.daftariqarzho.app.feature.debtors.domain.model.Debtor
import tj.daftariqarzho.app.feature.debtors.domain.repository.DebtorRepository

sealed interface SaveDebtorResult {

    data class Success(val debtorId: Long) : SaveDebtorResult

    data object EmptyName : SaveDebtorResult

    data object NameTooLong : SaveDebtorResult

    data object NotFound : SaveDebtorResult
}

class SaveDebtorUseCase(
    private val repository: DebtorRepository,
    private val now: () -> Long = System::currentTimeMillis,
) {

    suspend operator fun invoke(
        id: Long?,
        name: String,
        phone: String?,
        note: String?,
    ): SaveDebtorResult {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) return SaveDebtorResult.EmptyName
        if (trimmedName.length > MAX_NAME_LENGTH) return SaveDebtorResult.NameTooLong

        val trimmedPhone = phone?.trim()?.takeIf { it.isNotEmpty() }
        val trimmedNote = note?.trim()?.takeIf { it.isNotEmpty() }

        if (id == null) {
            val newId = repository.addDebtor(
                Debtor(
                    id = 0,
                    name = trimmedName,
                    phone = trimmedPhone,
                    note = trimmedNote,
                    createdAt = now(),
                ),
            )
            return SaveDebtorResult.Success(newId)
        }

        val existing = repository.getDebtor(id) ?: return SaveDebtorResult.NotFound
        repository.updateDebtor(
            existing.copy(
                name = trimmedName,
                phone = trimmedPhone,
                note = trimmedNote,
            ),
        )
        return SaveDebtorResult.Success(id)
    }

    companion object {
        const val MAX_NAME_LENGTH = 60
    }
}
