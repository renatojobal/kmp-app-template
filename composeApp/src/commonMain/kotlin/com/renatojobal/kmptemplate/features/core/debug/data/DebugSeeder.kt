package com.renatojobal.kmptemplate.features.core.debug.data

import com.renatojobal.kmptemplate.features.core.config.data.AppConfig
import com.renatojobal.kmptemplate.features.core.settings.domain.ISettingsStore
import com.renatojobal.kmptemplate.features.core.util.randomUUID
import com.renatojobal.kmptemplate.features.todo.domain.model.TodoDomain
import com.renatojobal.kmptemplate.features.todo.domain.repository.ITodoRepository
import kotlin.time.Clock

/**
 * Populates a couple of todos on first debug launch so the empty state isn't the
 * first thing a dev sees. Short-circuits on release builds via [AppConfig.isDebug].
 *
 * The "seeded" flag lives in [ISettingsStore] so wiping app data triggers a re-seed.
 */
class DebugSeeder(
    private val todoRepository: ITodoRepository,
    private val settingsStore: ISettingsStore,
) {
    suspend fun seedIfNeeded() {
        if (!AppConfig.isDebug) return
        if (settingsStore.getBoolean(KEY_SEEDED, default = false)) return

        val now = Clock.System.now()
        listOf(
            "Try this template" to false,
            "Rename the package" to false,
            "Replace this seed data" to true,
        ).forEach { (title, done) ->
            todoRepository.add(
                TodoDomain(
                    id = randomUUID(),
                    title = title,
                    isDone = done,
                    createdAt = now,
                )
            )
        }

        settingsStore.putBoolean(KEY_SEEDED, true)
    }

    companion object {
        private const val KEY_SEEDED = "debug_data_seeded"
    }
}
