package com.renatojobal.kmptemplate.features.core.logging.data

import com.renatojobal.kmptemplate.features.core.logging.domain.ICrashReporter
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel

/**
 * Antilog that forwards Napier WARN/ERROR/ASSERT events to Crashlytics.
 * Lower-priority events (DEBUG/INFO/VERBOSE) are dropped on the floor
 * to keep release-build noise low — DebugAntilog already handles those during dev.
 */
class CrashReporterAntilog(private val reporter: ICrashReporter) : Antilog() {
    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        if (priority < LogLevel.WARNING) return
        val tagPrefix = tag?.let { "[$it] " }.orEmpty()
        if (message != null) reporter.log("[${priority.name}] $tagPrefix$message")
        if (throwable != null) reporter.recordException(throwable)
    }
}
