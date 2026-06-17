package com.renatojobal.kmptemplate.features.core.error

import com.renatojobal.kmptemplate.features.core.error.domain.DataError
import com.renatojobal.kmptemplate.features.core.error.ui.UiText
import com.renatojobal.kmptemplate.features.core.error.ui.asUiText
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kmpapptemplate.composeapp.generated.resources.Res
import kmpapptemplate.composeapp.generated.resources.error_conflict
import kmpapptemplate.composeapp.generated.resources.error_network
import kmpapptemplate.composeapp.generated.resources.error_not_found
import kmpapptemplate.composeapp.generated.resources.error_unauthorized
import kmpapptemplate.composeapp.generated.resources.error_unexpected

class DataErrorUiTextTest : FunSpec({

    test("DataError.Remote.Network maps to error_network") {
        DataError.Remote.Network.asUiText() shouldBe UiText.FromRes(Res.string.error_network)
    }

    test("DataError.Remote.Unauthorized maps to error_unauthorized") {
        DataError.Remote.Unauthorized.asUiText() shouldBe UiText.FromRes(Res.string.error_unauthorized)
    }

    test("DataError.Remote.NotFound maps to error_not_found") {
        DataError.Remote.NotFound.asUiText() shouldBe UiText.FromRes(Res.string.error_not_found)
    }

    test("DataError.Local.NotFound maps to error_not_found") {
        DataError.Local.NotFound.asUiText() shouldBe UiText.FromRes(Res.string.error_not_found)
    }

    test("DataError.Remote.Conflict maps to error_conflict") {
        DataError.Remote.Conflict.asUiText() shouldBe UiText.FromRes(Res.string.error_conflict)
    }

    test("DataError.Remote.Serialization maps to error_unexpected") {
        DataError.Remote.Serialization.asUiText() shouldBe UiText.FromRes(Res.string.error_unexpected)
    }

    test("DataError.Remote.Unknown maps to error_unexpected") {
        DataError.Remote.Unknown.asUiText() shouldBe UiText.FromRes(Res.string.error_unexpected)
    }

    test("DataError.Local.Unknown maps to error_unexpected") {
        DataError.Local.Unknown.asUiText() shouldBe UiText.FromRes(Res.string.error_unexpected)
    }
})
