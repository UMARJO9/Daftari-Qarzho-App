package tj.daftariqarzho.app.feature.debtors.domain.model

fun List<DebtTransaction>.nearestUnpaidDueDate(): Long? {
    val unpaidDebts = ArrayDeque<UnpaidDebt>()
    var credit = 0L

    sortedWith(compareBy({ it.createdAt }, { it.id })).forEach { transaction ->
        if (transaction.amountInDirams < 0) {
            var remaining = -transaction.amountInDirams
            val covered = minOf(credit, remaining)
            credit -= covered
            remaining -= covered
            if (remaining > 0) unpaidDebts.addLast(UnpaidDebt(remaining, transaction.dueDate))
        } else {
            var payment = transaction.amountInDirams
            while (payment > 0 && unpaidDebts.isNotEmpty()) {
                val oldest = unpaidDebts.first()
                if (oldest.remaining <= payment) {
                    payment -= oldest.remaining
                    unpaidDebts.removeFirst()
                } else {
                    oldest.remaining -= payment
                    payment = 0
                }
            }
            credit += payment
        }
    }

    return unpaidDebts.mapNotNull { it.dueDate }.minOrNull()
}

private class UnpaidDebt(var remaining: Long, val dueDate: Long?)
