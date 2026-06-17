package com.renatojobal.kmptemplate.features.core.error.ui

import com.renatojobal.kmptemplate.features.core.error.domain.DataError
import kmpapptemplate.composeapp.generated.resources.Res
import kmpapptemplate.composeapp.generated.resources.error_conflict
import kmpapptemplate.composeapp.generated.resources.error_network
import kmpapptemplate.composeapp.generated.resources.error_not_found
import kmpapptemplate.composeapp.generated.resources.error_unauthorized
import kmpapptemplate.composeapp.generated.resources.error_unexpected

fun DataError.asUiText(): UiText = when (this) {
    DataError.Remote.Network -> UiText.FromRes(Res.string.error_network)
    DataError.Remote.Unauthorized -> UiText.FromRes(Res.string.error_unauthorized)
    DataError.Remote.NotFound,
    DataError.Local.NotFound -> UiText.FromRes(Res.string.error_not_found)
    DataError.Remote.Conflict -> UiText.FromRes(Res.string.error_conflict)
    DataError.Remote.Serialization,
    DataError.Remote.Unknown,
    DataError.Local.Unknown -> UiText.FromRes(Res.string.error_unexpected)
}
