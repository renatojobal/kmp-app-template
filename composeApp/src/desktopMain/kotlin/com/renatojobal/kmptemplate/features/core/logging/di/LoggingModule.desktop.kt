package com.renatojobal.kmptemplate.features.core.logging.di

import com.renatojobal.kmptemplate.features.core.logging.data.ConsoleCrashReporter
import com.renatojobal.kmptemplate.features.core.logging.domain.ICrashReporter
import org.koin.core.module.Module
import org.koin.dsl.module

actual val loggingPlatformModule: Module = module {
    single<ICrashReporter> { ConsoleCrashReporter() }
}
