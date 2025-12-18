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
import com.phasmofriend.app.model.Ghost
import com.phasmofriend.app.ui.shared.InvestigationViewModel

class ResultsFragment : Fragment() {

    private lateinit var resultsList: RecyclerView
    private lateinit var adapter: GhostCandidateAdapter

    // 🔗 Shared VM with Evidence/Behavior fragments & MainActivity
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

        resultsList = view.findViewById(R.id.results_list)

        adapter = GhostCandidateAdapter { ghost ->
            navigateToGhostDetail(ghost)
        }

        resultsList.layoutManager = LinearLayoutManager(requireContext())
        resultsList.adapter = adapter

        //
        // 🧠 Plug into real investigation data
        // Assumes your VM exposes LiveData<List<Ghost>> named `candidateGhosts`
        //
        investigationViewModel.candidates.observe(viewLifecycleOwner) { ghosts ->
            adapter.submitList(ghosts)
        }

        // 👉 If your VM uses a different API (e.g. a function),
        // you can change the above block to:
        //
        // val ghosts = investigationViewModel.getCandidateGhosts()
        // adapter.submitList(ghosts)
        //
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
