package com.renatojobal.kmptemplate.features.core.logging.data

import android.util.Log
import com.renatojobal.kmptemplate.features.core.logging.domain.ICrashReporter

/**
 * Template placeholder — writes to Logcat. Swap this for Firebase Crashlytics, Sentry,
 * Bugsnag, etc. by replacing the binding in [LoggingModule.android.kt] (and adding the
 * SDK to the androidMain dependencies).
 */
class LogcatCrashReporter : ICrashReporter {
    override fun log(message: String) {
        Log.e(TAG, message)
    }

    override fun recordException(throwable: Throwable) {
        Log.e(TAG, throwable.message ?: throwable::class.simpleName.orEmpty(), throwable)
    }

    private companion object {
        const val TAG = "Crash"
    }
}
