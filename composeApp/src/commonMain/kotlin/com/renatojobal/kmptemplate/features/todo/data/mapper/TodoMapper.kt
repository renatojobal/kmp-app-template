package com.renatojobal.kmptemplate.features.todo.data.mapper

import com.renatojobal.kmptemplate.db.Todo
import com.renatojobal.kmptemplate.features.todo.domain.model.TodoDomain
import kotlin.time.Instant

fun Todo.toDomain(): TodoDomain = TodoDomain(
    id = id,
    title = title,
    isDone = isDone != 0L,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
)

fun TodoDomain.toEntity(): Todo = Todo(
    id = id,
    title = title,
    isDone = if (isDone) 1L else 0L,
    createdAt = createdAt.toEpochMilliseconds(),
)
