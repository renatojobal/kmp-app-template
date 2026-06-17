package com.renatojobal.kmptemplate.features.core.logging.data

import com.renatojobal.kmptemplate.features.core.logging.domain.ICrashReporter
import platform.Foundation.NSLog

/**
 * Template placeholder — writes to NSLog. Swap this for Firebase Crashlytics, Sentry,
 * Bugsnag, etc. by replacing the binding in [LoggingModule.ios.kt]. Crashlytics for
 * iOS requires a Swift bridge — see the RetroJournal repo for the original pattern.
 */
class NSLogCrashReporter : ICrashReporter {
    override fun log(message: String) {
        NSLog("[Crash] %@", message)
    }

    override fun recordException(throwable: Throwable) {
        val name = throwable::class.simpleName ?: "Throwable"
        val reason = throwable.message ?: name
        NSLog("[Crash] %@: %@\n%@", name, reason, throwable.stackTraceToString())
    }
}
