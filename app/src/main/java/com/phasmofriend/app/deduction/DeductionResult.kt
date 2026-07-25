package com.phasmofriend.app.deduction

import com.phasmofriend.app.model.Ghost

/**
 * A scored ghost candidate for the current investigation.
 *
 * eliminated means a hard evidence contradiction was found. Eliminated ghosts
 * are still returned so the UI can explain why they dropped instead of simply
 * disappearing without context.
 */
data class DeductionResult(
    val ghost: Ghost,
    val score: Int,
    val matchPercent: Int,
    val eliminated: Boolean,
    val matchedReasons: List<String>,
    val warnings: List<String>,
    val contradictions: List<String>
)