package com.renatojobal.kmptemplate.features.todo.data.repository

import com.renatojobal.kmptemplate.features.todo.data.datasource.ITodoLocalDataSource
import com.renatojobal.kmptemplate.features.todo.data.mapper.toDomain
import com.renatojobal.kmptemplate.features.todo.data.mapper.toEntity
import com.renatojobal.kmptemplate.features.todo.domain.model.TodoDomain
import com.renatojobal.kmptemplate.features.todo.domain.repository.ITodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val local: ITodoLocalDataSource,
) : ITodoRepository {

    override fun observeAll(): Flow<List<TodoDomain>> =
        local.observeAll().map { rows -> rows.map { it.toDomain() } }

    override suspend fun add(todo: TodoDomain) {
        local.insert(todo.toEntity())
    }

    override suspend fun setDone(id: String, isDone: Boolean) {
        local.setDone(id, isDone)
    }

    override suspend fun delete(id: String) {
        local.deleteById(id)
    }
}
