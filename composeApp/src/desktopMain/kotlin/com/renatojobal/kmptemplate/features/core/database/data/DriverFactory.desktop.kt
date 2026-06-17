package com.renatojobal.kmptemplate.features.core.database.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.renatojobal.kmptemplate.db.AppDatabase
import com.renatojobal.kmptemplate.desktop.AppPaths
import java.io.File
import java.util.Properties

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val dbFile = File(AppPaths.dataDir, "app.db")
        val url = "jdbc:sqlite:${dbFile.absolutePath}"
        val driver = JdbcSqliteDriver(url, Properties())
        if (!dbFile.exists() || dbFile.length() == 0L) {
            AppDatabase.Schema.create(driver)
        }
        return driver
    }
}
