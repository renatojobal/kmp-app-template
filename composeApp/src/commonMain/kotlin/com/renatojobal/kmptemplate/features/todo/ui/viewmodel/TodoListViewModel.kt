package com.renatojobal.kmptemplate.features.todo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renatojobal.kmptemplate.features.todo.domain.usecase.IAddTodoUseCase
import com.renatojobal.kmptemplate.features.todo.domain.usecase.IDeleteTodoUseCase
import com.renatojobal.kmptemplate.features.todo.domain.usecase.IObserveTodosUseCase
import com.renatojobal.kmptemplate.features.todo.domain.usecase.IToggleTodoUseCase
import com.renatojobal.kmptemplate.features.todo.ui.state.TodoListAction
import com.renatojobal.kmptemplate.features.todo.ui.state.TodoListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val observeTodos: IObserveTodosUseCase,
    private val addTodo: IAddTodoUseCase,
    private val toggleTodo: IToggleTodoUseCase,
    private val deleteTodo: IDeleteTodoUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(TodoListState())
    val state: StateFlow<TodoListState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeTodos().collect { todos ->
                _state.update { it.copy(todos = todos, isLoading = false) }
            }
        }
    }

    fun onAction(action: TodoListAction) {
        when (action) {
            is TodoListAction.OnDraftChanged -> _state.update { it.copy(draftTitle = action.value) }
            TodoListAction.OnAddClick -> addCurrentDraft()
            is TodoListAction.OnToggle -> viewModelScope.launch {
                toggleTodo(action.id, action.isDone)
            }
            is TodoListAction.OnDelete -> viewModelScope.launch {
                deleteTodo(action.id)
            }
        }
    }

    private fun addCurrentDraft() {
        val draft = _state.value.draftTitle
        if (draft.isBlank()) return
        _state.update { it.copy(draftTitle = "") }
        viewModelScope.launch {
            addTodo(draft)
        }
    }
}
