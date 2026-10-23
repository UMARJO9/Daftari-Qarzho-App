package tj.daftariqarzho.app.feature.debtors.domain.model

data class DebtOverview(
    val givenInDirams: Long = 0,
    val receivedInDirams: Long = 0,
) {
    val balanceInDirams: Long get() = receivedInDirams - givenInDirams

    val returnedFraction: Float
        get() = if (givenInDirams <= 0) {
            0f
        } else {
            (receivedInDirams.toDouble() / givenInDirams).coerceIn(0.0, 1.0).toFloat()
        }
}
