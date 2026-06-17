package com.renatojobal.kmptemplate.features.core.logging.di

import com.renatojobal.kmptemplate.features.core.logging.data.NapierInitializer
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Platform module provides [com.renatojobal.kmptemplate.features.core.logging.domain.ICrashReporter]:
 * - androidMain → LogcatCrashReporter (swap for Crashlytics/Sentry/Bugsnag)
 * - iosMain     → NSLogCrashReporter  (swap for Crashlytics via a Swift bridge)
 * - desktopMain → ConsoleCrashReporter
 */
expect val loggingPlatformModule: Module

val loggingModule = module {
    single { NapierInitializer(get()) }
}
