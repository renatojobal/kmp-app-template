package com.renatojobal.kmptemplate.desktop

import java.io.File

/**
 * Returns a per-OS user-data directory. Created on first access.
 *
 * macOS:   ~/Library/Application Support/KmpAppTemplate
 * Windows: %APPDATA%/KmpAppTemplate (or %LOCALAPPDATA% / ~/AppData/Roaming as fallback)
 * Linux:   $XDG_DATA_HOME/kmpapptemplate (or ~/.local/share/kmpapptemplate)
 */
object AppPaths {
    val dataDir: File by lazy {
        val os = System.getProperty("os.name").lowercase()
        val home = System.getProperty("user.home")
        val dir = when {
            os.contains("mac") -> File(home, "Library/Application Support/KmpAppTemplate")
            os.contains("win") -> {
                val appData = System.getenv("APPDATA") ?: "$home/AppData/Roaming"
                File(appData, "KmpAppTemplate")
            }
            else -> {
                val xdg = System.getenv("XDG_DATA_HOME") ?: "$home/.local/share"
                File(xdg, "kmpapptemplate")
            }
        }
        dir.apply { mkdirs() }
    }
}
