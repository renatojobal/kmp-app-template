package com.renatojobal.kmptemplate.features.core.error.domain

sealed interface DataError {
    sealed interface Remote : DataError {
        data object Network : Remote
        data object Unauthorized : Remote
        data object NotFound : Remote
        data object Conflict : Remote
        data object Serialization : Remote
        data object Unknown : Remote
    }

    sealed interface Local : DataError {
        data object NotFound : Local
        data object Unknown : Local
    }
}
