package com.renatojobal.kmptemplate

import android.app.Application
import org.koin.android.ext.koin.androidContext

class TemplateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initApp { androidContext(this@TemplateApplication) }
    }
}
