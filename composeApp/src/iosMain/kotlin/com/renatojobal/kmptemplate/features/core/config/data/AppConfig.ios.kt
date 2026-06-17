package com.renatojobal.kmptemplate.features.core.config.data

import platform.Foundation.NSBundle

@OptIn(kotlin.experimental.ExperimentalNativeApi::class)
actual object AppConfig {
    actual val isDebug: Boolean = kotlin.native.Platform.isDebugBinary
    actual val appVersionName: String =
        NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "0.0.0"
    actual val appVersionCode: Int =
        (NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion") as? String)?.toIntOrNull() ?: 0
}
