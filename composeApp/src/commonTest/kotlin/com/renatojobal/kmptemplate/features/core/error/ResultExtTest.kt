package com.renatojobal.kmptemplate.features.core.error

import com.renatojobal.kmptemplate.features.core.error.domain.DataError
import com.renatojobal.kmptemplate.features.core.error.domain.Result
import com.renatojobal.kmptemplate.features.core.error.domain.asEmpty
import com.renatojobal.kmptemplate.features.core.error.domain.errorOrNull
import com.renatojobal.kmptemplate.features.core.error.domain.getOrNull
import com.renatojobal.kmptemplate.features.core.error.domain.map
import com.renatojobal.kmptemplate.features.core.error.domain.onFailure
import com.renatojobal.kmptemplate.features.core.error.domain.onSuccess
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ResultExtTest : FunSpec({

    test("map transforms a Success") {
        val r: Result<Int, DataError> = Result.Success(2)
        r.map { it * 3 } shouldBe Result.Success(6)
    }

    test("map preserves a Failure") {
        val r: Result<Int, DataError> = Result.Failure(DataError.Remote.Network)
        r.map { it * 3 } shouldBe Result.Failure(DataError.Remote.Network)
    }

    test("onSuccess fires only on Success") {
        var fired = false
        Result.Success<Int>(1).onSuccess { fired = true }
        fired shouldBe true

        fired = false
        Result.Failure(DataError.Remote.Unknown).onSuccess { fired = true }
        fired shouldBe false
    }

    test("onFailure fires only on Failure") {
        var captured: DataError? = null
        Result.Failure(DataError.Remote.Conflict).onFailure { captured = it }
        captured shouldBe DataError.Remote.Conflict

        captured = null
        Result.Success<Int>(1).onFailure { captured = it }
        captured shouldBe null
    }

    test("getOrNull returns value on Success and null on Failure") {
        Result.Success(42).getOrNull() shouldBe 42
        val failed: Result<Int, DataError> = Result.Failure(DataError.Remote.Network)
        failed.getOrNull() shouldBe null
    }

    test("errorOrNull returns error on Failure and null on Success") {
        val failed: Result<Int, DataError> = Result.Failure(DataError.Remote.Unauthorized)
        failed.errorOrNull() shouldBe DataError.Remote.Unauthorized
        Result.Success(1).errorOrNull() shouldBe null
    }

    test("asEmpty drops the success payload") {
        Result.Success(42).asEmpty() shouldBe Result.Success(Unit)
        val failed: Result<Int, DataError> = Result.Failure(DataError.Remote.NotFound)
        failed.asEmpty() shouldBe Result.Failure(DataError.Remote.NotFound)
    }
})
