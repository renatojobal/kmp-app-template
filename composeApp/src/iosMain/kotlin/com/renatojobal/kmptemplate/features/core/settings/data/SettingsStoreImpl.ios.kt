package com.renatojobal.kmptemplate.features.core.settings.data

import com.renatojobal.kmptemplate.features.core.settings.domain.ISettingsStore

import platform.Foundation.NSUserDefaults

actual class SettingsStoreImpl : ISettingsStore {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual override fun getBoolean(key: String, default: Boolean): Boolean =
        if (defaults.objectForKey(key) != null) {
            defaults.boolForKey(key)
        } else {
            default
        }

    actual override fun putBoolean(key: String, value: Boolean) {
        defaults.setBool(value, forKey = key)
    }

    actual override fun getString(key: String, default: String?): String? =
        defaults.stringForKey(key) ?: default

    actual override fun putString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    actual override fun getInt(key: String, default: Int): Int =
        if (defaults.objectForKey(key) != null) {
            defaults.integerForKey(key).toInt()
        } else {
            default
        }

    actual override fun putInt(key: String, value: Int) {
        defaults.setInteger(value.toLong(), forKey = key)
    }

    actual override fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }
}
