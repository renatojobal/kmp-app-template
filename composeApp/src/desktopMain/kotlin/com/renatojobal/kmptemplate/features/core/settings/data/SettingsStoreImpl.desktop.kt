package com.renatojobal.kmptemplate.features.core.settings.data

import com.renatojobal.kmptemplate.desktop.AppPaths
import com.renatojobal.kmptemplate.features.core.settings.domain.ISettingsStore
import java.io.File
import java.util.Properties

actual class SettingsStoreImpl : ISettingsStore {
    private val file: File = File(AppPaths.dataDir, "settings.properties")
    private val props = Properties().also { p ->
        if (file.exists()) file.inputStream().use { p.load(it) }
    }

    @Synchronized
    private fun persist() {
        file.outputStream().use { props.store(it, "kmptemplate settings") }
    }

    actual override fun getBoolean(key: String, default: Boolean): Boolean =
        props.getProperty(key)?.toBooleanStrictOrNull() ?: default

    actual override fun putBoolean(key: String, value: Boolean) {
        props.setProperty(key, value.toString())
        persist()
    }

    actual override fun getString(key: String, default: String?): String? =
        props.getProperty(key) ?: default

    actual override fun putString(key: String, value: String) {
        props.setProperty(key, value)
        persist()
    }

    actual override fun getInt(key: String, default: Int): Int =
        props.getProperty(key)?.toIntOrNull() ?: default

    actual override fun putInt(key: String, value: Int) {
        props.setProperty(key, value.toString())
        persist()
    }

    actual override fun remove(key: String) {
        if (props.remove(key) != null) persist()
    }
}
