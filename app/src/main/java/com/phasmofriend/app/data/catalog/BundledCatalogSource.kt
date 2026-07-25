package com.phasmofriend.app.data.catalog

import android.content.Context
import kotlinx.serialization.json.Json
import java.util.Locale

/**
 * Offline fallback: the same 27 ghosts / 34 behaviors that used to live in
 * GhostRepository/BehaviorTag, now bundled as JSON so the app works fully
 * offline on first launch, before Firestore ever answers.
 */
class BundledCatalogSource(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    fun load(): CatalogSnapshot {
        val text = context.assets.open(SEED_ASSET_NAME).bufferedReader().use { it.readText() }
        val seed = json.decodeFromString(CatalogSeed.serializer(), text)
        val locale = Locale.getDefault().language
        val behaviors = seed.behaviors.map { it.toBehavior(locale) }
        return seed.ghosts.toCatalog(locale, behaviors)
    }

    private companion object {
        const val SEED_ASSET_NAME = "catalog_seed.json"
    }
}
