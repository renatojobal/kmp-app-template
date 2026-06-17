package com.renatojobal.kmptemplate.features.todo.domain.usecase

import com.renatojobal.kmptemplate.features.core.util.randomUUID
import com.renatojobal.kmptemplate.features.todo.domain.model.TodoDomain
import com.renatojobal.kmptemplate.features.todo.domain.repository.ITodoRepository
import kotlin.time.Clock

interface IAddTodoUseCase {
    suspend operator fun invoke(title: String)
}

class AddTodoUseCaseImpl(
    private val repository: ITodoRepository,
) : IAddTodoUseCase {
    override suspend fun invoke(title: String) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return
        repository.add(
            TodoDomain(
                id = randomUUID(),
                title = trimmed,
                isDone = false,
                createdAt = Clock.System.now(),
            )
        )
    }
}
