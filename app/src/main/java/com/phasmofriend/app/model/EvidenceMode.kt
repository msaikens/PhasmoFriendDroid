package com.phasmofriend.app.model

enum class EvidenceMode(
    val displayName: String,
    val visibleEvidenceCount: Int,
    val canHardEliminateRuledOutEvidence: Boolean,
    val shouldRecommendEvidenceTests: Boolean
) {
    STANDARD(
        displayName = "Standard",
        visibleEvidenceCount = 3,
        canHardEliminateRuledOutEvidence = true,
        shouldRecommendEvidenceTests = true
    ),

    NIGHTMARE(
        displayName = "Nightmare",
        visibleEvidenceCount = 2,
        canHardEliminateRuledOutEvidence = false,
        shouldRecommendEvidenceTests = true
    ),

    INSANITY(
        displayName = "Insanity",
        visibleEvidenceCount = 1,
        canHardEliminateRuledOutEvidence = false,
        shouldRecommendEvidenceTests = true
    ),

    ZERO_EVIDENCE(
        displayName = "Zero Evidence",
        visibleEvidenceCount = 0,
        canHardEliminateRuledOutEvidence = false,
        shouldRecommendEvidenceTests = false
    );

    fun canRecommendMoreEvidence(confirmedEvidenceCount: Int): Boolean {
        return shouldRecommendEvidenceTests &&
                confirmedEvidenceCount < visibleEvidenceCount
    }
}