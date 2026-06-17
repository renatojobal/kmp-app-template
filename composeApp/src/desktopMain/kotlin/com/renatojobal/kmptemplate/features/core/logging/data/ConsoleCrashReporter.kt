package com.renatojobal.kmptemplate.features.core.logging.data

import com.renatojobal.kmptemplate.features.core.logging.domain.ICrashReporter

/**
 * MVP desktop crash reporter — no Crashlytics on desktop.
 *
 * Writes directly to System.err. Must NOT call Napier here: NapierInitializer
 * registers a CrashReporterAntilog that forwards WARN/ERROR Napier events into
 * this reporter, so logging via Napier would re-enter and StackOverflow.
 */
class ConsoleCrashReporter : ICrashReporter {
    override fun log(message: String) {
        System.err.println("[crash] $message")
    }

    override fun recordException(throwable: Throwable) {
        System.err.println("[crash] ${throwable::class.simpleName}: ${throwable.message}")
        throwable.printStackTrace(System.err)
    }
}
