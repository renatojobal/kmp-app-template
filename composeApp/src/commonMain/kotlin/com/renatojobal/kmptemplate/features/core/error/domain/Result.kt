package com.renatojobal.kmptemplate.features.core.error.domain

sealed interface Result<out D, out E : DataError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E : DataError>(val error: E) : Result<Nothing, E>
}

typealias EmptyResult<E> = Result<Unit, E>
