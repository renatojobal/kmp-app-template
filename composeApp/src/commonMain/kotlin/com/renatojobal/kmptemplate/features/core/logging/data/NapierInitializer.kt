package com.renatojobal.kmptemplate.features.core.logging.data

import com.renatojobal.kmptemplate.features.core.config.data.AppConfig
import com.renatojobal.kmptemplate.features.core.logging.domain.ICrashReporter
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

/**
 * Initializes the Napier logging facade once at app startup.
 *
 * Debug builds: DebugAntilog only → Logcat (Android) / NSLog (iOS).
 * Release builds: DebugAntilog stays on for parity, plus CrashReporterAntilog
 * which forwards WARN/ERROR events to Firebase Crashlytics.
 */
class NapierInitializer(
    private val crashReporter: ICrashReporter,
) {
    fun init() {
        Napier.base(DebugAntilog())
        if (!AppConfig.isDebug) {
            Napier.base(CrashReporterAntilog(crashReporter))
        }
    }
}
