package com.renatojobal.kmptemplate.features.core.error.domain

inline fun <T, E : DataError, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Failure -> this
}

inline fun <T, E : DataError> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T, E : DataError> Result<T, E>.onFailure(action: (E) -> Unit): Result<T, E> {
    if (this is Result.Failure) action(error)
    return this
}

fun <T, E : DataError> Result<T, E>.getOrNull(): T? = (this as? Result.Success)?.data

fun <E : DataError> Result<*, E>.errorOrNull(): E? = (this as? Result.Failure)?.error

fun <T, E : DataError> Result<T, E>.asEmpty(): EmptyResult<E> = map { }
