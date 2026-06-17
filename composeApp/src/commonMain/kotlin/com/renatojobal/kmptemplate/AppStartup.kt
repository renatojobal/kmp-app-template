package com.renatojobal.kmptemplate

import com.renatojobal.kmptemplate.di.appModules
import com.renatojobal.kmptemplate.features.core.debug.data.DebugSeeder
import com.renatojobal.kmptemplate.features.core.logging.data.NapierInitializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Single entry point for Koin + Napier initialization, called exactly once at platform
 * startup:
 *   - Android: from [com.renatojobal.kmptemplate.TemplateApplication.onCreate] (passes
 *     androidContext via [platformConfiguration]).
 *   - iOS:     from iOSApp.swift.
 *   - Desktop: from Main.kt before [androidx.compose.ui.window.application].
 */
fun initApp(platformConfiguration: KoinAppDeclaration = {}) {
    val koinApp = startKoin {
        platformConfiguration()
        modules(appModules)
    }
    koinApp.koin.get<NapierInitializer>().init()

    val seeder = koinApp.koin.get<DebugSeeder>()
    CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
        seeder.seedIfNeeded()
    }
}
