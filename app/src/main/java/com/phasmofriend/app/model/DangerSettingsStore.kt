// app/src/main/java/com/phasmofriend/app/model/DangerSettingsStore.java
package com.phasmofriend.app.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.phasmofriend.app.R;

import kotlin.jvm.JvmStatic;

public final class DangerSettingsStore {

    // These are populated from resources on first use
    private static String PREF_NAME = "danger_settings";
    private static String KEY_PREFIX = "danger_level_";

    // Private constructor — this class should never be instantiated
    private DangerSettingsStore() {
    }

    /**
     * Load preference name and key prefix from string resources.
     * Called automatically before accessing SharedPreferences.
     */
    private static void loadKeysFromResources(@NonNull Context ctx) {
        PREF_NAME = ctx.getString(R.string.danger_settings);
        KEY_PREFIX = ctx.getString(R.string.danger_level_prefix);
    }

    @NonNull
    private static SharedPreferences prefs(@NonNull Context ctx) {
        loadKeysFromResources(ctx);
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @NonNull
    private static String keyFor(@NonNull Ghost ghost) {
        return KEY_PREFIX + ghost.getNameResId();
    }

    /**
     * MUST be static so Kotlin can call:
     * DangerSettingsStore.getEffectiveDangerLevel(context, ghost)
     */
    @JvmStatic
    @NonNull
    public static DangerLevel getEffectiveDangerLevel(
            @NonNull Context ctx,
            @NonNull Ghost ghost
    ) {
        SharedPreferences p = prefs(ctx);
        String stored = p.getString(keyFor(ghost), null);

        if (stored != null) {
            try {
                return DangerLevel.valueOf(stored);
            } catch (IllegalArgumentException ignored) {
                // Corrupted / outdated value — fall back to default
            }
        }

        // Fallback: use the ghost's built-in default danger level
        return ghost.getDangerLevel();
    }

    /**
     * MUST be static so Kotlin can call:
     * DangerSettingsStore.setDangerLevel(context, ghost, level)
     * If newLevel is null or equals the default ghost.getDangerLevel(),
     * the override is removed and we revert to default behavior.
     */
    @JvmStatic
    public static void setDangerLevel(
            @NonNull Context ctx,
            @NonNull Ghost ghost,
            @Nullable DangerLevel newLevel
    ) {
        SharedPreferences p = prefs(ctx);
        SharedPreferences.Editor e = p.edit();

        String key = keyFor(ghost);
        DangerLevel defaultLevel = ghost.getDangerLevel();

        if (newLevel == null || newLevel == defaultLevel) {
            // No need to store default; clear any override
            e.remove(key);
        } else {
            e.putString(key, newLevel.name());
        }

        e.apply();
    }
}
