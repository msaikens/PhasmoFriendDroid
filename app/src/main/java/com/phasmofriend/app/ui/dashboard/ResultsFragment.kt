// app/src/main/java/com/phasmofriend/app/ui/dashboard/ResultsFragment.kt
package com.phasmofriend.app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phasmofriend.app.R
import com.phasmofriend.app.model.Evidence
import com.phasmofriend.app.model.EvidenceState
import com.phasmofriend.app.model.Ghost
import com.phasmofriend.app.model.GhostRepository
import com.phasmofriend.app.ui.shared.InvestigationViewModel

class ResultsFragment : Fragment() {

    private lateinit var resultsList: RecyclerView
    private lateinit var adapter: GhostCandidateAdapter

    private val investigationViewModel: InvestigationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ FIXED ID (matches fragment_results.xml)
        resultsList = view.findViewById(R.id.results_list)

        adapter = GhostCandidateAdapter { ghost ->
            navigateToGhostDetail(ghost)
        }

        resultsList.layoutManager = LinearLayoutManager(requireContext())
        resultsList.adapter = adapter

        // ✅ Always recompute from source list when evidence state changes
        investigationViewModel.evidenceStates.observe(viewLifecycleOwner) { evidenceStates ->
            val base = GhostRepository.ghosts
            val filtered = filterByEvidenceTriState(base, evidenceStates)
            adapter.submitList(filtered)
        }

        // ✅ initial load (in case evidenceStates already has a value)
        val initialStates = investigationViewModel.evidenceStates.value.orEmpty()
        adapter.submitList(filterByEvidenceTriState(GhostRepository.ghosts, initialStates))
    }

    private fun filterByEvidenceTriState(
        ghosts: List<Ghost>,
        evidenceStates: Map<Evidence, EvidenceState>
    ): List<Ghost> {
        val required = evidenceStates.filterValues { it == EvidenceState.HAS }.keys
        val excluded = evidenceStates.filterValues { it == EvidenceState.NOT }.keys

        return ghosts.filter { ghost ->
            val ev = ghost.evidences.toSet()
            required.all { it in ev } && excluded.none { it in ev }
        }
    }

    private fun navigateToGhostDetail(ghost: Ghost) {
        val args = bundleOf(
            "ghostNameResId" to ghost.nameResId,
            "ghostDescriptionResId" to ghost.descriptionResId
        )
        findNavController().navigate(
            R.id.action_navigation_results_to_ghostDetailFragment,
            args
        )
    }
}
