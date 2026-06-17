package com.renatojobal.kmptemplate.di

import com.renatojobal.kmptemplate.features.core.database.data.DriverFactory
import com.renatojobal.kmptemplate.features.core.settings.data.SettingsStoreImpl
import com.renatojobal.kmptemplate.features.core.settings.domain.ISettingsStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DriverFactory() }
    single<ISettingsStore> { SettingsStoreImpl() }
}
