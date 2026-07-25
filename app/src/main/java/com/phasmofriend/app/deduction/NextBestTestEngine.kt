package com.phasmofriend.app.deduction

import com.phasmofriend.app.model.Behavior
import com.phasmofriend.app.model.Evidence
import com.phasmofriend.app.model.Ghost

data class DismissedClues(
    val evidence: Set<Evidence> = emptySet(),
    val behaviorIds: Set<String> = emptySet()
)

class NextBestTestEngine {

    fun recommend(
        candidates: List<DeductionResult>,
        input: InvestigationInput,
        dismissed: DismissedClues = DismissedClues()
    ): NextBestTest {
        val activeCandidates = candidates.filter { result -> !result.eliminated }

        if (!input.hasAnySignals) {
            return if (input.evidenceMode.shouldRecommendEvidenceTests) {
                NextBestTest(
                    title = "Start with broad evidence",
                    detail = "You are in ${input.evidenceMode.displayName} mode. Start by checking the easiest evidence types first, then I can suggest a sharper split.",
                    meta = "${input.evidenceMode.displayName} mode • ${input.evidenceMode.visibleEvidenceCount} visible evidence clue(s).",
                    subject = TestSubject.None
                )
            } else {
                NextBestTest(
                    title = "Start with behavior",
                    detail = "You are in Zero Evidence mode. Ignore normal evidence hunting and focus on sanity threshold, hunt speed, blink pattern, salt behavior, roaming, and unique abilities.",
                    meta = "Zero Evidence mode • behavior-first deduction.",
                    subject = TestSubject.None
                )
            }
        }

        if (activeCandidates.isEmpty()) {
            return NextBestTest(
                title = "Re-check your assumptions",
                detail = "Every ghost has been eliminated. A confirmed evidence clue may be wrong, or a ruled-out clue may be unreliable for ${input.evidenceMode.displayName} mode.",
                meta = "${input.evidenceMode.displayName} mode • 0 active candidates.",
                subject = TestSubject.None
            )
        }

        if (activeCandidates.size == 1) {
            return recommendConfirmationTest(
                candidate = activeCandidates.first(),
                input = input,
                dismissed = dismissed
            )
        }

        val shouldUseEvidence =
            input.evidenceMode.canRecommendMoreEvidence(input.confirmedEvidence.size)

        if (shouldUseEvidence) {
            val evidenceSplit = findBestEvidenceSplit(
                candidates = activeCandidates,
                input = input,
                dismissed = dismissed
            )

            if (evidenceSplit != null) {
                return NextBestTest(
                    title = "Test for ${evidenceSplit.evidence.readableName()}",
                    detail = "This is the cleanest next evidence check because it separates ${evidenceSplit.yesCount} likely ghost(s) from ${evidenceSplit.noCount} likely ghost(s).",
                    meta = "${input.evidenceMode.displayName} mode • ${activeCandidates.size} active candidates remaining.",
                    subject = TestSubject.EvidenceClue(evidenceSplit.evidence)
                )
            }
        }

        val behaviorSplit = findBestBehaviorSignal(activeCandidates, input, dismissed)

        if (behaviorSplit != null) {
            return NextBestTest(
                title = "Watch how the ghost behaves",
                detail = "${behaviorSplit.behavior.label} This matches ${behaviorSplit.count} of the remaining candidates.",
                meta = "${input.evidenceMode.displayName} mode • ${activeCandidates.size} active candidates remaining.",
                subject = TestSubject.BehaviorClue(behaviorSplit.behavior.id)
            )
        }

        return NextBestTest(
            title = "Use behavior to break the tie",
            detail = "The remaining ghosts are too close by evidence. Watch hunt speed, line-of-sight acceleration, sanity threshold, salt behavior, DOTS behavior, or unusual interactions.",
            meta = "${input.evidenceMode.displayName} mode • ${activeCandidates.size} active candidates remaining.",
            subject = TestSubject.None
        )
    }

    private fun recommendConfirmationTest(
        candidate: DeductionResult,
        input: InvestigationInput,
        dismissed: DismissedClues
    ): NextBestTest {
        val ghost = candidate.ghost

        val canStillUseEvidence =
            input.evidenceMode.canRecommendMoreEvidence(input.confirmedEvidence.size)

        if (canStillUseEvidence) {
            val missingRequiredEvidence = ghost.evidences
                .firstOrNull { evidence ->
                    evidence !in input.confirmedEvidence &&
                            evidence !in input.ruledOutEvidence &&
                            evidence !in dismissed.evidence
                }

            if (missingRequiredEvidence != null) {
                return NextBestTest(
                    title = "Confirm ${missingRequiredEvidence.readableName()}",
                    detail = "Only one active candidate remains. Confirming this evidence gives you a safer final call.",
                    meta = "${input.evidenceMode.displayName} mode • 1 active candidate remaining.",
                    subject = TestSubject.EvidenceClue(missingRequiredEvidence)
                )
            }
        }

        val specialBehavior = ghost.possibleBehaviors
            .firstOrNull { behavior ->
                behavior.id !in dismissed.behaviorIds &&
                        behavior !in input.observedBehaviors &&
                        behavior !in input.ruledOutBehaviors
            }

        if (specialBehavior != null) {
            return NextBestTest(
                title = "Confirm this behavior",
                detail = "${specialBehavior.label} Only one active candidate remains — confirming this behavior protects against a bad evidence read.",
                meta = "${input.evidenceMode.displayName} mode • 1 active candidate remaining.",
                subject = TestSubject.BehaviorClue(specialBehavior.id)
            )
        }

        return NextBestTest(
            title = "Ready to decide",
            detail = "Only one active candidate remains based on your current clues.",
            meta = "${input.evidenceMode.displayName} mode • 1 active candidate remaining.",
            subject = TestSubject.None
        )
    }

    private fun findBestEvidenceSplit(
        candidates: List<DeductionResult>,
        input: InvestigationInput,
        dismissed: DismissedClues
    ): EvidenceSplit? {
        val unknownEvidence = Evidence.entries
            .filter { evidence ->
                evidence !in input.confirmedEvidence &&
                        evidence !in input.ruledOutEvidence &&
                        evidence !in dismissed.evidence
            }

        return unknownEvidence
            .mapNotNull { evidence ->
                val yesCount = candidates.count { result ->
                    result.ghost.supportsEvidence(evidence)
                }

                val noCount = candidates.size - yesCount
                val splitScore = minOf(yesCount, noCount)

                if (splitScore <= 0) {
                    null
                } else {
                    EvidenceSplit(
                        evidence = evidence,
                        yesCount = yesCount,
                        noCount = noCount,
                        splitScore = splitScore
                    )
                }
            }
            .maxWithOrNull(
                compareBy<EvidenceSplit> { split -> split.splitScore }
                    .thenBy { split -> split.evidence.name }
            )
    }

    private fun findBestBehaviorSignal(
        candidates: List<DeductionResult>,
        input: InvestigationInput,
        dismissed: DismissedClues
    ): BehaviorSignal? {
        return candidates
            .flatMap { result -> result.ghost.possibleBehaviors }
            .filter { behavior ->
                behavior.id !in dismissed.behaviorIds &&
                        behavior !in input.observedBehaviors &&
                        behavior !in input.ruledOutBehaviors
            }
            .groupingBy { behavior -> behavior }
            .eachCount()
            .map { entry ->
                BehaviorSignal(
                    behavior = entry.key,
                    count = entry.value
                )
            }
            .filter { signal -> signal.count > 0 }
            .maxWithOrNull(
                compareBy<BehaviorSignal> { signal -> signal.count }
                    .thenBy { signal -> signal.behavior.id }
            )
    }

    private fun Ghost.supportsEvidence(evidence: Evidence): Boolean {
        return evidence in evidences || evidence in extraEvidence
    }

    private fun Evidence.readableName(): String = shortName

    private data class EvidenceSplit(
        val evidence: Evidence,
        val yesCount: Int,
        val noCount: Int,
        val splitScore: Int
    )

    private data class BehaviorSignal(
        val behavior: Behavior,
        val count: Int
    )
}
