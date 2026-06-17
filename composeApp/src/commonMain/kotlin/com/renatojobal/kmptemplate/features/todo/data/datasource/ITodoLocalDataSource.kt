package com.renatojobal.kmptemplate.features.todo.data.datasource

import com.renatojobal.kmptemplate.db.Todo
import kotlinx.coroutines.flow.Flow

interface ITodoLocalDataSource {
    fun observeAll(): Flow<List<Todo>>
    suspend fun insert(todo: Todo)
    suspend fun setDone(id: String, isDone: Boolean)
    suspend fun deleteById(id: String)
}
