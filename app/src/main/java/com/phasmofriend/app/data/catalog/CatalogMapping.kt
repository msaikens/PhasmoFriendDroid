package com.phasmofriend.app.data.catalog

import com.phasmofriend.app.model.Behavior
import com.phasmofriend.app.model.DangerLevel
import com.phasmofriend.app.model.Evidence
import com.phasmofriend.app.model.Ghost

data class CatalogSnapshot(
    val ghosts: List<Ghost> = emptyList(),
    val behaviors: List<Behavior> = emptyList()
)

private fun Map<String, String>.resolve(locale: String, fallback: String): String =
    this[locale] ?: fallback

fun BehaviorDoc.toBehavior(locale: String): Behavior = Behavior(
    id = id,
    label = labelTranslations.resolve(locale, label),
    weight = weight
)

/**
 * Unknown evidence/behavior references (typos, or content newer than this app
 * build knows about) are skipped rather than crashing the whole catalog load.
 */
fun GhostDoc.toGhost(locale: String, behaviorsById: Map<String, Behavior>): Ghost = Ghost(
    id = id,
    name = nameTranslations.resolve(locale, name),
    evidences = evidence.mapNotNullTo(linkedSetOf()) { it.toEvidenceOrNull() },
    extraEvidence = extraEvidence.mapNotNullTo(linkedSetOf()) { it.toEvidenceOrNull() },
    possibleBehaviors = behaviors.mapNotNullTo(linkedSetOf()) { behaviorsById[it] },
    description = descriptionTranslations.resolve(locale, description),
    dangerLevel = runCatching { DangerLevel.valueOf(dangerLevel) }.getOrDefault(DangerLevel.LOW)
)

private fun String.toEvidenceOrNull(): Evidence? = runCatching { Evidence.valueOf(this) }.getOrNull()

fun List<GhostDoc>.toCatalog(locale: String, behaviors: List<Behavior>): CatalogSnapshot {
    val behaviorsById = behaviors.associateBy { it.id }
    val ghosts = sortedBy { it.order }.map { it.toGhost(locale, behaviorsById) }
    return CatalogSnapshot(ghosts = ghosts, behaviors = behaviors)
}
