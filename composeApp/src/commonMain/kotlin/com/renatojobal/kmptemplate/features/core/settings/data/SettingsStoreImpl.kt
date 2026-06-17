package com.renatojobal.kmptemplate.features.core.settings.data

import com.renatojobal.kmptemplate.features.core.settings.domain.ISettingsStore

expect class SettingsStoreImpl : ISettingsStore {
    override fun getBoolean(key: String, default: Boolean): Boolean
    override fun putBoolean(key: String, value: Boolean)
    override fun getString(key: String, default: String?): String?
    override fun putString(key: String, value: String)
    override fun getInt(key: String, default: Int): Int
    override fun putInt(key: String, value: Int)
    override fun remove(key: String)
}
