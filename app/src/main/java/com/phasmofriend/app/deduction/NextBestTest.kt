package com.phasmofriend.app.deduction

import com.phasmofriend.app.model.Evidence

/** What a NextBestTest is actually about, so the UI can act on Passed/Failed/Inconclusive. */
sealed interface TestSubject {
    data class EvidenceClue(val evidence: Evidence) : TestSubject
    data class BehaviorClue(val behaviorId: String) : TestSubject
    data object None : TestSubject
}

data class NextBestTest(
    val title: String,
    val detail: String,
    val meta: String,
    val subject: TestSubject
)
