package com.renatojobal.kmptemplate.features.core.settings.data

import com.renatojobal.kmptemplate.features.core.settings.domain.ISettingsStore

import android.content.Context
import android.content.SharedPreferences

actual class SettingsStoreImpl(context: Context) : ISettingsStore {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("kmptemplate_settings", Context.MODE_PRIVATE)

    actual override fun getBoolean(key: String, default: Boolean): Boolean =
        prefs.getBoolean(key, default)

    actual override fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    actual override fun getString(key: String, default: String?): String? =
        prefs.getString(key, default)

    actual override fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    actual override fun getInt(key: String, default: Int): Int =
        prefs.getInt(key, default)

    actual override fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    actual override fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}
