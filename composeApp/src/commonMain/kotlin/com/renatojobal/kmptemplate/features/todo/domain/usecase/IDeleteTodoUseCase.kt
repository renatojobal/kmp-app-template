package com.renatojobal.kmptemplate.features.todo.domain.usecase

import com.renatojobal.kmptemplate.features.todo.domain.repository.ITodoRepository

interface IDeleteTodoUseCase {
    suspend operator fun invoke(id: String)
}

class DeleteTodoUseCaseImpl(
    private val repository: ITodoRepository,
) : IDeleteTodoUseCase {
    override suspend fun invoke(id: String) {
        repository.delete(id)
    }
}
