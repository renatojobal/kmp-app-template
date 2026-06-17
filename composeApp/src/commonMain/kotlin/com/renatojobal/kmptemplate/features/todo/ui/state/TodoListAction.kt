package com.renatojobal.kmptemplate.features.todo.ui.state

sealed interface TodoListAction {
    data class OnDraftChanged(val value: String) : TodoListAction
    data object OnAddClick : TodoListAction
    data class OnToggle(val id: String, val isDone: Boolean) : TodoListAction
    data class OnDelete(val id: String) : TodoListAction
}
