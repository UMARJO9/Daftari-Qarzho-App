package tj.daftariqarzho.app.core.common.result

/**
 * Универсальная обёртка результата операции: успех, ошибка или загрузка.
 *
 * Используется на границах слоёв (data → presentation), чтобы явно
 * моделировать три состояния без исключений в UI.
 */
sealed interface DaftarResult<out T> {

    /** Успешный результат с данными [data]. */
    data class Success<T>(val data: T) : DaftarResult<T>

    /** Ошибка с причиной [throwable]. */
    data class Error(val throwable: Throwable) : DaftarResult<Nothing>

    /** Операция выполняется. */
    data object Loading : DaftarResult<Nothing>
}

/** Возвращает данные при успехе либо `null` в остальных случаях. */
fun <T> DaftarResult<T>.getOrNull(): T? =
    (this as? DaftarResult.Success)?.data

/** Оборачивает [block] в [DaftarResult], перехватывая исключения. */
inline fun <T> runCatchingResult(block: () -> T): DaftarResult<T> =
    try {
        DaftarResult.Success(block())
    } catch (t: Throwable) {
        DaftarResult.Error(t)
    }
