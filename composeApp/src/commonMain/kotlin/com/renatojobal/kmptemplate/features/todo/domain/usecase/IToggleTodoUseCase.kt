package com.renatojobal.kmptemplate.features.todo.domain.usecase

import com.renatojobal.kmptemplate.features.todo.domain.repository.ITodoRepository

interface IToggleTodoUseCase {
    suspend operator fun invoke(id: String, isDone: Boolean)
}

class ToggleTodoUseCaseImpl(
    private val repository: ITodoRepository,
) : IToggleTodoUseCase {
    override suspend fun invoke(id: String, isDone: Boolean) {
        repository.setDone(id, isDone)
    }
}
