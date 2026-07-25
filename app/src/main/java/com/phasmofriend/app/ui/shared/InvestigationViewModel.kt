package com.phasmofriend.app.ui.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.phasmofriend.app.data.catalog.CatalogRepository
import com.phasmofriend.app.data.catalog.CatalogRepositoryImpl
import com.phasmofriend.app.data.catalog.CatalogStatus
import com.phasmofriend.app.deduction.DeductionEngine
import com.phasmofriend.app.deduction.DeductionResult
import com.phasmofriend.app.deduction.DismissedClues
import com.phasmofriend.app.deduction.InvestigationInput
import com.phasmofriend.app.deduction.NextBestTest
import com.phasmofriend.app.deduction.NextBestTestEngine
import com.phasmofriend.app.deduction.TestSubject
import com.phasmofriend.app.model.Behavior
import com.phasmofriend.app.model.DangerLevel
import com.phasmofriend.app.model.DangerSettingsStore
import com.phasmofriend.app.model.Evidence
import com.phasmofriend.app.model.EvidenceMode
import com.phasmofriend.app.model.EvidenceState
import com.phasmofriend.app.model.Ghost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class InvestigationUiState(
    val catalogStatus: CatalogStatus = CatalogStatus.LOADING,
    val evidenceMode: EvidenceMode = EvidenceMode.STANDARD,
    val evidenceStates: Map<Evidence, EvidenceState> = emptyMap(),
    val selectedBehaviorIds: Set<String> = emptySet(),
    val behaviors: List<Behavior> = emptyList(),
    val candidates: List<DeductionResult> = emptyList(),
    val nextBestTest: NextBestTest? = null,
    val dangerOverrides: Map<String, DangerLevel> = emptyMap()
) {
    fun dangerLevelFor(ghost: Ghost): DangerLevel = dangerOverrides[ghost.id] ?: ghost.dangerLevel
}

class InvestigationViewModel(application: Application) : AndroidViewModel(application) {

    private val catalogRepository: CatalogRepository =
        CatalogRepositoryImpl(context = application, externalScope = viewModelScope)

    private val deductionEngine = DeductionEngine()
    private val nextBestTestEngine = NextBestTestEngine()

    private val evidenceMode = MutableStateFlow(EvidenceMode.STANDARD)
    private val evidenceStates = MutableStateFlow<Map<Evidence, EvidenceState>>(emptyMap())
    private val selectedBehaviorIds = MutableStateFlow<Set<String>>(emptySet())

    // Clues dismissed as "inconclusive" for the current investigation state, so the
    // engine surfaces a different suggestion instead of repeating the same one.
    private val dismissedEvidence = MutableStateFlow<Set<Evidence>>(emptySet())
    private val dismissedBehaviorIds = MutableStateFlow<Set<String>>(emptySet())

    private val evidenceInput = combine(evidenceStates, dismissedEvidence) { states, dismissed -> states to dismissed }
    private val behaviorInput = combine(selectedBehaviorIds, dismissedBehaviorIds) { selected, dismissed -> selected to dismissed }

    val uiState: StateFlow<InvestigationUiState> = combine(
        catalogRepository.state,
        evidenceMode,
        evidenceInput,
        behaviorInput,
        DangerSettingsStore.overrides(application)
    ) { catalog, mode, (states, dismissedEv), (selectedIds, dismissedBehIds), overrides ->
        val input = InvestigationInput(
            evidenceMode = mode,
            confirmedEvidence = states.filterValues { it == EvidenceState.HAS }.keys,
            ruledOutEvidence = states.filterValues { it == EvidenceState.NOT }.keys,
            observedBehaviors = catalog.behaviors.filter { it.id in selectedIds }.toSet()
        )

        val candidates = deductionEngine.evaluate(catalog.ghosts, input)
        val dismissed = DismissedClues(evidence = dismissedEv, behaviorIds = dismissedBehIds)

        InvestigationUiState(
            catalogStatus = catalog.status,
            evidenceMode = mode,
            evidenceStates = states,
            selectedBehaviorIds = selectedIds,
            behaviors = catalog.behaviors,
            candidates = candidates,
            nextBestTest = nextBestTestEngine.recommend(candidates, input, dismissed),
            dangerOverrides = overrides
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InvestigationUiState()
    )

    fun setEvidenceMode(mode: EvidenceMode) {
        evidenceMode.value = mode
        clearDismissed()
    }

    fun getEvidenceState(evidence: Evidence): EvidenceState =
        evidenceStates.value[evidence] ?: EvidenceState.OFF

    fun setEvidenceState(evidence: Evidence, state: EvidenceState) {
        val current = evidenceStates.value.toMutableMap()
        if (state == EvidenceState.OFF) current.remove(evidence) else current[evidence] = state
        evidenceStates.value = current
        clearDismissed()
    }

    fun cycleEvidence(evidence: Evidence) {
        val next = when (getEvidenceState(evidence)) {
            EvidenceState.OFF -> EvidenceState.HAS
            EvidenceState.HAS -> EvidenceState.NOT
            EvidenceState.NOT -> EvidenceState.OFF
        }
        setEvidenceState(evidence, next)
    }

    fun toggleBehavior(behaviorId: String) {
        val current = selectedBehaviorIds.value.toMutableSet()
        if (!current.add(behaviorId)) current.remove(behaviorId)
        selectedBehaviorIds.value = current
        clearDismissed()
    }

    fun clearAll() {
        evidenceStates.value = emptyMap()
        selectedBehaviorIds.value = emptySet()
        clearDismissed()
    }

    fun setDangerLevel(ghost: Ghost, newLevel: DangerLevel?) {
        viewModelScope.launch {
            DangerSettingsStore.setDangerLevel(getApplication(), ghost, newLevel)
        }
    }

    /** The suggested clue was observed/confirmed. */
    fun markTestPassed(subject: TestSubject) {
        when (subject) {
            is TestSubject.EvidenceClue -> setEvidenceState(subject.evidence, EvidenceState.HAS)
            is TestSubject.BehaviorClue -> {
                if (subject.behaviorId !in selectedBehaviorIds.value) toggleBehavior(subject.behaviorId)
            }
            TestSubject.None -> Unit
        }
    }

    /** The suggested clue was checked and did not occur. */
    fun markTestFailed(subject: TestSubject) {
        when (subject) {
            is TestSubject.EvidenceClue -> setEvidenceState(subject.evidence, EvidenceState.NOT)
            is TestSubject.BehaviorClue -> {
                if (subject.behaviorId in selectedBehaviorIds.value) toggleBehavior(subject.behaviorId)
            }
            TestSubject.None -> Unit
        }
    }

    /** Couldn't tell either way — don't change any evidence/behavior, just suggest something else. */
    fun markTestInconclusive(subject: TestSubject) {
        when (subject) {
            is TestSubject.EvidenceClue -> dismissedEvidence.value = dismissedEvidence.value + subject.evidence
            is TestSubject.BehaviorClue -> dismissedBehaviorIds.value = dismissedBehaviorIds.value + subject.behaviorId
            TestSubject.None -> Unit
        }
    }

    private fun clearDismissed() {
        dismissedEvidence.value = emptySet()
        dismissedBehaviorIds.value = emptySet()
    }
}
