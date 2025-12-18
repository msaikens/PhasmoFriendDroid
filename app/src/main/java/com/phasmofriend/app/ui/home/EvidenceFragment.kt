package com.phasmofriend.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.phasmofriend.app.R
import com.phasmofriend.app.model.Evidence
import com.phasmofriend.app.ui.shared.InvestigationViewModel
import java.util.Locale

class EvidenceFragment : Fragment() {

    private val viewModel: InvestigationViewModel by activityViewModels()

    private lateinit var containerLayout: LinearLayout

    // Sorted list of all evidences
    private lateinit var allEvidence: List<Evidence>

    // Row bundle: one per evidence
    private data class EvidenceRow(
        val evidence: Evidence,
        val rowView: View,
        val iconView: ImageView,
        val checkBox: CheckBox
    )

    private val rows: MutableList<EvidenceRow> = mutableListOf()

    companion object {
        // UI source-of-truth for EvidenceFragment selection
        private val evidenceSelection: MutableSet<Evidence> = mutableSetOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_evidence, container, false)

        containerLayout = view.findViewById(R.id.evidence_checkbox_container)

        val ctx = requireContext()

        // Build sorted list once for stable display
        allEvidence = Evidence.entries
            .sortedBy { ev ->
                ctx.getString(ev.labelResId).lowercase(Locale.getDefault())
            }

        // Seed our local selection store from ViewModel snapshot
        evidenceSelection.clear()
        evidenceSelection.addAll(viewModel.selectedEvidence.value.orEmpty())

        // Build all rows once
        buildAllRows()

        return view
    }

    /**
     * Inflate and attach one row per Evidence, using the local
     * evidenceSelection for initial checked state.
     */
    private fun buildAllRows() {
        containerLayout.removeAllViews()
        rows.clear()

        val inflater = LayoutInflater.from(requireContext())

        allEvidence.forEach { evidence ->
            val row = inflater.inflate(
                R.layout.item_evidence_option,
                containerLayout,
                false
            )

            val iconView = row.findViewById<ImageView>(R.id.evidence_icon)
            val checkBox = row.findViewById<CheckBox>(R.id.evidence_checkbox)

            // Bind icon + label
            iconView.setImageResource(evidence.iconResId)
            checkBox.text = getString(evidence.labelResId)

            // Initial state from our local selection
            checkBox.isChecked = evidenceSelection.contains(evidence)

            // Single toggle handler for both row and checkbox
            val toggle: (View) -> Unit = {
                val newChecked = !evidenceSelection.contains(evidence)

                if (newChecked) {
                    evidenceSelection.add(evidence)
                } else {
                    evidenceSelection.remove(evidence)
                }

                checkBox.isChecked = newChecked
                viewModel.setEvidenceChecked(evidence, newChecked)
            }

            // Click row or checkbox → same toggle logic
            row.setOnClickListener(toggle)
            checkBox.setOnClickListener(toggle)

            val evidenceRow = EvidenceRow(
                evidence = evidence,
                rowView = row,
                iconView = iconView,
                checkBox = checkBox
            )

            rows += evidenceRow
            containerLayout.addView(row)
        }
    }

    /**
     * Called from MainActivity when user chooses "Clear All".
     * MainActivity has already called viewModel.clearAll(), so here we just
     * clear our UI + local selection store.
     */
    fun clearSelections() {
        evidenceSelection.clear()

        rows.forEach { row ->
            row.checkBox.isChecked = false
        }
    }
}
