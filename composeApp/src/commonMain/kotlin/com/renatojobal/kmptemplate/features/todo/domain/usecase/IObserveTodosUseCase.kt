package com.renatojobal.kmptemplate.features.todo.domain.usecase

import com.renatojobal.kmptemplate.features.todo.domain.model.TodoDomain
import com.renatojobal.kmptemplate.features.todo.domain.repository.ITodoRepository
import kotlinx.coroutines.flow.Flow

interface IObserveTodosUseCase {
    operator fun invoke(): Flow<List<TodoDomain>>
}

class ObserveTodosUseCaseImpl(
    private val repository: ITodoRepository,
) : IObserveTodosUseCase {
    override fun invoke(): Flow<List<TodoDomain>> = repository.observeAll()
}
