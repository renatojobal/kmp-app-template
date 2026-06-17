package com.renatojobal.kmptemplate.features.core.error.data

import com.renatojobal.kmptemplate.features.core.error.domain.DataError
import com.renatojobal.kmptemplate.features.core.error.domain.Result
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CancellationException

suspend inline fun <T> safeCall(
    tag: String = "call",
    block: () -> T
): Result<T, DataError> = try {
    Result.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    Napier.e(message = "[$tag] failed", throwable = e)
    Result.Failure(DataError.Remote.Unknown)
}
