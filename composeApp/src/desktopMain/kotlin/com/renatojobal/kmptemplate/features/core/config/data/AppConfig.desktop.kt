package com.renatojobal.kmptemplate.features.core.config.data

actual object AppConfig {
    actual val isDebug: Boolean =
        System.getenv("KMPTEMPLATE_DEBUG")?.equals("true", ignoreCase = true) == true

    actual val appVersionName: String =
        System.getProperty("kmptemplate.versionName") ?: "0.1.0"

    actual val appVersionCode: Int =
        System.getProperty("kmptemplate.versionCode")?.toIntOrNull() ?: 1
}
