package com.renatojobal.kmptemplate.features.todo.data.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.renatojobal.kmptemplate.db.AppDatabase
import com.renatojobal.kmptemplate.db.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TodoLocalDataSourceImpl(
    database: AppDatabase,
) : ITodoLocalDataSource {

    private val queries = database.todoQueries

    override fun observeAll(): Flow<List<Todo>> =
        queries.selectAll().asFlow().mapToList(Dispatchers.Default)

    override suspend fun insert(todo: Todo) {
        withContext(Dispatchers.Default) {
            queries.insert(
                id = todo.id,
                title = todo.title,
                isDone = todo.isDone,
                createdAt = todo.createdAt,
            )
        }
    }

    override suspend fun setDone(id: String, isDone: Boolean) {
        withContext(Dispatchers.Default) {
            queries.updateDone(isDone = if (isDone) 1L else 0L, id = id)
        }
    }

    override suspend fun deleteById(id: String) {
        withContext(Dispatchers.Default) {
            queries.deleteById(id)
        }
    }
}
