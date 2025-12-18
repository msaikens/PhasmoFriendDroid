package com.phasmofriend.app.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phasmofriend.app.model.BehaviorTag
import com.phasmofriend.app.model.Evidence
import com.phasmofriend.app.model.Ghost
import com.phasmofriend.app.model.GhostRepository

class InvestigationViewModel : ViewModel() {

    private val _selectedEvidence = MutableLiveData<Set<Evidence>>(emptySet())
    val selectedEvidence: LiveData<Set<Evidence>> = _selectedEvidence

    private val _selectedBehaviors = MutableLiveData<Set<BehaviorTag>>(emptySet())
    val selectedBehaviors: LiveData<Set<BehaviorTag>> = _selectedBehaviors

    private val _candidates = MutableLiveData(GhostRepository.ghosts)
    val candidates: LiveData<List<Ghost>> = _candidates

    fun setEvidenceChecked(evidence: Evidence, isChecked: Boolean) {
        val current = _selectedEvidence.value.orEmpty().toMutableSet()
        if (isChecked) current.add(evidence) else current.remove(evidence)
        _selectedEvidence.value = current
        updateCandidates()
    }

    fun setBehaviorChecked(tag: BehaviorTag, isChecked: Boolean) {
        println("setBehaviorChecked: $tag -> $isChecked; now = ${_selectedBehaviors.value}")
        val current = _selectedBehaviors.value.orEmpty().toMutableSet()
        if (isChecked) current.add(tag) else current.remove(tag)
        _selectedBehaviors.value = current
        updateCandidates()
    }

    fun clearAll() {
        _selectedEvidence.value = emptySet()
        _selectedBehaviors.value = emptySet()
        _candidates.value = GhostRepository.ghosts
    }

    private fun updateCandidates() {
        val evidence = _selectedEvidence.value.orEmpty()
        val behavior = _selectedBehaviors.value.orEmpty()

        val filtered = GhostRepository.ghosts.filter { ghost ->
            val allEvidence: Set<Evidence> =
                ghost.extraEvidence.let { ghost.evidences + it }

            val evidenceMatch = evidence.all { it in allEvidence }
            val behaviorMatch = behavior.all { it in ghost.possibleBehaviors }

            evidenceMatch && behaviorMatch
        }

        _candidates.value =
            if (filtered.isEmpty() && evidence.isEmpty() && behavior.isEmpty()) {
                GhostRepository.ghosts
            } else {
                filtered
            }
    }
}
