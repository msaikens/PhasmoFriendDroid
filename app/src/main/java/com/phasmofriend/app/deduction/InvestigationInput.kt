package com.phasmofriend.app.deduction

import com.phasmofriend.app.model.Behavior
import com.phasmofriend.app.model.Evidence
import com.phasmofriend.app.model.EvidenceMode

data class InvestigationInput(
    val evidenceMode: EvidenceMode = EvidenceMode.STANDARD,
    val confirmedEvidence: Set<Evidence> = emptySet(),
    val ruledOutEvidence: Set<Evidence> = emptySet(),
    val observedBehaviors: Set<Behavior> = emptySet()
) {
    val hasAnySignals: Boolean
        get() = confirmedEvidence.isNotEmpty() ||
                ruledOutEvidence.isNotEmpty() ||
                observedBehaviors.isNotEmpty()
}