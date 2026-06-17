package com.renatojobal.kmptemplate.features.todo.domain.repository

import com.renatojobal.kmptemplate.features.todo.domain.model.TodoDomain
import kotlinx.coroutines.flow.Flow

interface ITodoRepository {
    fun observeAll(): Flow<List<TodoDomain>>
    suspend fun add(todo: TodoDomain)
    suspend fun setDone(id: String, isDone: Boolean)
    suspend fun delete(id: String)
}
