package com.phasmofriend.app.data.catalog

import kotlinx.serialization.Serializable

/**
 * Raw document shapes shared by the bundled seed asset and Firestore.
 * `id` is read directly from the bundled JSON, but overwritten with the
 * Firestore document id for remote-sourced entries (see FirestoreCatalogSource) -
 * console edits don't need to duplicate the id inside the document body.
 */
@Serializable
data class GhostDoc(
    val id: String = "",
    val name: String = "",
    val nameTranslations: Map<String, String> = emptyMap(),
    val description: String = "",
    val descriptionTranslations: Map<String, String> = emptyMap(),
    val evidence: List<String> = emptyList(),
    val extraEvidence: List<String> = emptyList(),
    val behaviors: List<String> = emptyList(),
    val dangerLevel: String = "LOW",
    val order: Int = 0
)

@Serializable
data class BehaviorDoc(
    val id: String = "",
    val label: String = "",
    val labelTranslations: Map<String, String> = emptyMap(),
    val weight: Int = 0
)

@Serializable
data class CatalogSeed(
    val ghosts: List<GhostDoc> = emptyList(),
    val behaviors: List<BehaviorDoc> = emptyList()
)
