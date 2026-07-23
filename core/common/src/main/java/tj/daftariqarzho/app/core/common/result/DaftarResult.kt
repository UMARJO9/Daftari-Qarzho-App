package tj.daftariqarzho.app.core.common.result

sealed interface DaftarResult<out T> {

    data class Success<T>(val data: T) : DaftarResult<T>

    data class Error(val throwable: Throwable) : DaftarResult<Nothing>

    data object Loading : DaftarResult<Nothing>
}

fun <T> DaftarResult<T>.getOrNull(): T? =
    (this as? DaftarResult.Success)?.data

inline fun <T> runCatchingResult(block: () -> T): DaftarResult<T> =
    try {
        DaftarResult.Success(block())
    } catch (t: Throwable) {
        DaftarResult.Error(t)
    }
