package com.renatojobal.kmptemplate.features.todo.ui.state

import com.renatojobal.kmptemplate.features.todo.domain.model.TodoDomain

data class TodoListState(
    val todos: List<TodoDomain> = emptyList(),
    val draftTitle: String = "",
    val isLoading: Boolean = true,
)
