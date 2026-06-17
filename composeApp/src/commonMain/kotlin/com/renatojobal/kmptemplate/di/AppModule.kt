package com.renatojobal.kmptemplate.di

import com.renatojobal.kmptemplate.db.AppDatabase
import com.renatojobal.kmptemplate.features.core.database.data.DriverFactory
import com.renatojobal.kmptemplate.features.core.debug.data.DebugSeeder
import com.renatojobal.kmptemplate.features.core.logging.di.loggingModule
import com.renatojobal.kmptemplate.features.core.logging.di.loggingPlatformModule
import com.renatojobal.kmptemplate.features.todo.di.todoModule
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val databaseModule = module {
    single { get<DriverFactory>().createDriver() }
    single { AppDatabase(get()) }
}

val debugModule = module {
    single { DebugSeeder(get(), get()) }
}

val appModules = listOf(
    databaseModule,
    platformModule,
    loggingPlatformModule,
    loggingModule,
    todoModule,
    debugModule,
)
