package com.renatojobal.kmptemplate

class JvmPlatform : Platform {
    override val name: String =
        "${System.getProperty("os.name")} ${System.getProperty("os.version")}"
}

actual fun getPlatform(): Platform = JvmPlatform()
