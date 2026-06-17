package com.renatojobal.kmptemplate.features.core.error

import com.renatojobal.kmptemplate.features.core.error.data.safeCall
import com.renatojobal.kmptemplate.features.core.error.domain.DataError
import com.renatojobal.kmptemplate.features.core.error.domain.Result
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CancellationException

class SafeCallTest : FunSpec({

    test("wraps a successful block in Result.Success") {
        val result = safeCall { 42 }
        result shouldBe Result.Success(42)
    }

    test("maps any Throwable to DataError.Remote.Unknown") {
        val result = safeCall<Int> { throw IllegalStateException("boom") }
        result shouldBe Result.Failure(DataError.Remote.Unknown)
    }

    test("rethrows CancellationException (structured concurrency)") {
        shouldThrow<CancellationException> {
            safeCall<Int> { throw CancellationException("cancelled") }
        }
    }
})
