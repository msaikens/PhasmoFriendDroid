package com.phasmofriend.app.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dangerSettingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "danger_settings"
)

/**
 * Per-user danger-level overrides, keyed by ghost id. Purely local — this is a
 * personal annotation, never synced to Firestore alongside the shared catalog.
 */
object DangerSettingsStore {

    private const val KEY_PREFIX = "danger_level_"

    private fun keyFor(ghostId: String) = stringPreferencesKey(KEY_PREFIX + ghostId)

    /** All current overrides, keyed by ghost id. Missing entries mean "use the ghost's own default". */
    fun overrides(context: Context): Flow<Map<String, DangerLevel>> {
        return context.dangerSettingsDataStore.data.map { prefs ->
            prefs.asMap().entries.mapNotNull { (key, value) ->
                val ghostId = key.name.removePrefix(KEY_PREFIX).takeIf { key.name.startsWith(KEY_PREFIX) }
                val level = (value as? String)?.let { runCatching { DangerLevel.valueOf(it) }.getOrNull() }
                if (ghostId != null && level != null) ghostId to level else null
            }.toMap()
        }
    }

    suspend fun setDangerLevel(context: Context, ghost: Ghost, newLevel: DangerLevel?) {
        context.dangerSettingsDataStore.edit { prefs ->
            if (newLevel == null || newLevel == ghost.dangerLevel) {
                prefs.remove(keyFor(ghost.id))
            } else {
                prefs[keyFor(ghost.id)] = newLevel.name
            }
        }
    }
}
