package com.renatojobal.kmptemplate.features.todo.di

import com.renatojobal.kmptemplate.features.todo.data.datasource.ITodoLocalDataSource
import com.renatojobal.kmptemplate.features.todo.data.datasource.TodoLocalDataSourceImpl
import com.renatojobal.kmptemplate.features.todo.data.repository.TodoRepositoryImpl
import com.renatojobal.kmptemplate.features.todo.domain.repository.ITodoRepository
import com.renatojobal.kmptemplate.features.todo.domain.usecase.AddTodoUseCaseImpl
import com.renatojobal.kmptemplate.features.todo.domain.usecase.DeleteTodoUseCaseImpl
import com.renatojobal.kmptemplate.features.todo.domain.usecase.IAddTodoUseCase
import com.renatojobal.kmptemplate.features.todo.domain.usecase.IDeleteTodoUseCase
import com.renatojobal.kmptemplate.features.todo.domain.usecase.IObserveTodosUseCase
import com.renatojobal.kmptemplate.features.todo.domain.usecase.IToggleTodoUseCase
import com.renatojobal.kmptemplate.features.todo.domain.usecase.ObserveTodosUseCaseImpl
import com.renatojobal.kmptemplate.features.todo.domain.usecase.ToggleTodoUseCaseImpl
import com.renatojobal.kmptemplate.features.todo.ui.viewmodel.TodoListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val todoModule = module {
    single<ITodoLocalDataSource> { TodoLocalDataSourceImpl(get()) }
    single<ITodoRepository> { TodoRepositoryImpl(get()) }

    single<IObserveTodosUseCase> { ObserveTodosUseCaseImpl(get()) }
    single<IAddTodoUseCase> { AddTodoUseCaseImpl(get()) }
    single<IToggleTodoUseCase> { ToggleTodoUseCaseImpl(get()) }
    single<IDeleteTodoUseCase> { DeleteTodoUseCaseImpl(get()) }

    viewModelOf(::TodoListViewModel)
}
