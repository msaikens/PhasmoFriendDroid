package com.phasmofriend.app.deduction

import com.phasmofriend.app.model.Evidence
import com.phasmofriend.app.model.EvidenceMode
import com.phasmofriend.app.model.Ghost

class DeductionEngine {

    fun evaluate(
        ghosts: List<Ghost>,
        input: InvestigationInput
    ): List<DeductionResult> {
        return ghosts
            .map { ghost -> evaluateGhost(ghost, input) }
            .sortedWith(
                compareBy<DeductionResult> { it.eliminated }
                    .thenByDescending { it.matchPercent }
                    .thenByDescending { it.score }
                    .thenBy { it.ghost.id }
            )
    }

    private fun evaluateGhost(
        ghost: Ghost,
        input: InvestigationInput
    ): DeductionResult {
        val reasons = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        val contradictions = mutableListOf<String>()

        var rawScore = 0
        var eliminated = false

        if (
            input.evidenceMode != EvidenceMode.STANDARD &&
            input.confirmedEvidence.size > input.evidenceMode.visibleEvidenceCount
        ) {
            warnings += "${input.evidenceMode.displayName} normally exposes only ${input.evidenceMode.visibleEvidenceCount} evidence clue(s). Re-check selected evidence."
            rawScore -= 10
        }

        input.confirmedEvidence.forEach { evidence ->
            when {
                evidence in ghost.evidences -> {
                    rawScore += 30
                    reasons += "${evidence.label()} matches this ghost's evidence."
                }

                evidence in ghost.extraEvidence -> {
                    rawScore += 12
                    warnings += "${evidence.label()} can appear as extra or misleading evidence for this ghost."
                }

                else -> {
                    rawScore -= 45
                    eliminated = true
                    contradictions += "${evidence.label()} does not match this ghost's evidence."
                }
            }
        }

        input.ruledOutEvidence.forEach { evidence ->
            when {
                evidence in ghost.evidences -> {
                    if (input.evidenceMode.canHardEliminateRuledOutEvidence) {
                        rawScore -= 35
                        eliminated = true
                        contradictions += "You ruled out ${evidence.label()}, but this ghost normally requires it."
                    } else {
                        rawScore -= 12
                        warnings += "You ruled out ${evidence.label()}, but ${input.evidenceMode.displayName} can hide normal evidence."
                    }
                }

                evidence in ghost.extraEvidence -> {
                    rawScore -= 10
                    warnings += "You ruled out ${evidence.label()}, which this ghost can show as extra evidence."
                }

                else -> {
                    rawScore += 6
                    reasons += "Ruling out ${evidence.label()} does not conflict with this ghost."
                }
            }
        }

        input.observedBehaviors.forEach { behavior ->
            if (behavior in ghost.possibleBehaviors) {
                rawScore += behavior.weight
                reasons += "${behavior.label} matches known behavior."
            } else {
                rawScore -= 5
                contradictions += "${behavior.label} is not a known behavior for this ghost."
            }
        }

        val score = rawScore.coerceAtLeast(0)

        val matchPercent = if (input.hasAnySignals) {
            (50 + rawScore).coerceIn(0, 100)
        } else {
            0
        }

        return DeductionResult(
            ghost = ghost,
            score = score,
            matchPercent = matchPercent,
            eliminated = eliminated,
            matchedReasons = reasons.distinct(),
            warnings = warnings.distinct(),
            contradictions = contradictions.distinct()
        )
    }

    private fun Evidence.label(): String = shortName
}