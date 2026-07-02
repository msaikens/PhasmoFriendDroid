package com.phasmofriend.app.ui.home

import android.graphics.Paint
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
import com.phasmofriend.app.model.EvidenceState   // <-- your enum: OFF, HAS, NOT
import com.phasmofriend.app.ui.shared.InvestigationViewModel
import java.util.Locale

class EvidenceFragment : Fragment() {

    private val viewModel: InvestigationViewModel by activityViewModels()

    private lateinit var containerLayout: LinearLayout
    private lateinit var allEvidence: List<Evidence>

    private data class EvidenceRow(
        val evidence: Evidence,
        val rowView: View,
        val iconView: ImageView,
        val checkBox: CheckBox
    )

    private val rows: MutableList<EvidenceRow> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_evidence, container, false)
        containerLayout = view.findViewById(R.id.evidence_checkbox_container)

        val ctx = requireContext()

        allEvidence = Evidence.entries.sortedBy { ev ->
            ctx.getString(ev.labelResId).lowercase(Locale.getDefault())
        }

        buildAllRows()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Re-apply row UI any time evidence states change
        viewModel.evidenceStates.observe(viewLifecycleOwner) { stateMap ->
            rows.forEach { row ->
                val state = stateMap[row.evidence] ?: EvidenceState.OFF
                applyRowState(row, state)
            }
        }
    }

    private fun buildAllRows() {
        containerLayout.removeAllViews()
        rows.clear()

        val inflater = LayoutInflater.from(requireContext())

        allEvidence.forEach { evidence ->
            val rowView = inflater.inflate(
                R.layout.item_evidence_option,
                containerLayout,
                false
            )

            val iconView = rowView.findViewById<ImageView>(R.id.evidence_icon)
            val checkBox = rowView.findViewById<CheckBox>(R.id.evidence_checkbox)

            iconView.setImageResource(evidence.iconResId)
            checkBox.text = getString(evidence.labelResId)

            val evidenceRow = EvidenceRow(
                evidence = evidence,
                rowView = rowView,
                iconView = iconView,
                checkBox = checkBox
            )

            // Tap cycles: OFF -> HAS -> NOT -> OFF
            val cycle: (View) -> Unit = {
                val current = viewModel.getEvidenceState(evidence)
                val next = when (current) {
                    EvidenceState.OFF -> EvidenceState.HAS
                    EvidenceState.HAS -> EvidenceState.NOT
                    EvidenceState.NOT -> EvidenceState.OFF
                }
                viewModel.setEvidenceState(evidence, next)
            }

            rowView.setOnClickListener(cycle)
            checkBox.setOnClickListener(cycle)

            rows += evidenceRow
            containerLayout.addView(rowView)

            // Apply initial UI state immediately (before LiveData emits)
            val initial = viewModel.getEvidenceState(evidence)
            applyRowState(evidenceRow, initial)
        }
    }

    private fun applyRowState(row: EvidenceRow, state: EvidenceState) {
        when (state) {
            EvidenceState.OFF -> {
                row.checkBox.isChecked = false
                row.checkBox.paintFlags = row.checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                row.rowView.alpha = 1.0f
                row.iconView.alpha = 1.0f
                row.checkBox.alpha = 1.0f
            }

            EvidenceState.HAS -> {
                row.checkBox.isChecked = true
                row.checkBox.paintFlags = row.checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                row.rowView.alpha = 1.0f
                row.iconView.alpha = 1.0f
                row.checkBox.alpha = 1.0f
            }

            EvidenceState.NOT -> {
                // Strike-through state
                row.checkBox.isChecked = false
                row.checkBox.paintFlags = row.checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                row.rowView.alpha = 0.75f
                row.iconView.alpha = 0.75f
                row.checkBox.alpha = 0.75f
            }
        }
    }

    fun clearSelections() {
        // MainActivity already called viewModel.clearAll()
        // This function only needs to refresh UI state.
        rows.forEach { row ->
            applyRowState(row, EvidenceState.OFF)
        }
    }
}
