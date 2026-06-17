package com.renatojobal.kmptemplate.features.core.config.data

import com.renatojobal.kmptemplate.BuildConfig

actual object AppConfig {
    actual val isDebug: Boolean = BuildConfig.DEBUG
    actual val appVersionName: String = BuildConfig.VERSION_NAME
    actual val appVersionCode: Int = BuildConfig.VERSION_CODE
}
