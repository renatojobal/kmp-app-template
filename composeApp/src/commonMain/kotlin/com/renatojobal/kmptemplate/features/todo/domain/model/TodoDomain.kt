package com.renatojobal.kmptemplate.features.todo.domain.model

import kotlin.time.Instant

data class TodoDomain(
    val id: String,
    val title: String,
    val isDone: Boolean,
    val createdAt: Instant,
)
