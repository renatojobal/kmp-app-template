package com.renatojobal.kmptemplate.features.core.config.data

expect object AppConfig {
    val isDebug: Boolean
    val appVersionName: String
    val appVersionCode: Int
}
