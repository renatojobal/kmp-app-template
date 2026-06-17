package com.renatojobal.kmptemplate.features.core.logging.domain

/**
 * Platform-agnostic crash reporter contract.
 *
 * Template ships with simple log-only impls (Logcat / NSLog / println). Swap them with
 * a real SDK (Crashlytics, Sentry, Bugsnag) by replacing the bindings in each platform's
 * `loggingPlatformModule`.
 *
 * In release builds, [CrashReporterAntilog] forwards Napier WARN/ERROR events through
 * this interface — so anywhere code calls `Napier.e(...)`, the configured reporter sees it.
 */
interface ICrashReporter {
    fun log(message: String)
    fun recordException(throwable: Throwable)
}
