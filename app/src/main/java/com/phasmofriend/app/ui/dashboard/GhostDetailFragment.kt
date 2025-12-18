package com.phasmofriend.app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.phasmofriend.app.R
import com.phasmofriend.app.model.DangerLevel
import com.phasmofriend.app.model.DangerSettingsStore
import com.phasmofriend.app.model.GhostRepository

class GhostDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_ghost_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameView = view.findViewById<TextView>(R.id.text_ghost_detail_name) ?: return
        val evidenceView = view.findViewById<TextView>(R.id.text_ghost_detail_evidence) ?: return
        val behaviorsView = view.findViewById<TextView>(R.id.text_ghost_detail_behaviors) ?: return
        val descriptionView = view.findViewById<TextView>(R.id.text_ghost_detail_description) ?: return
        val backButton = view.findViewById<Button>(R.id.button_back_results) ?: return

        val dangerGroup = view.findViewById<RadioGroup>(R.id.group_danger_level) ?: return
        val radioLow = view.findViewById<RadioButton>(R.id.radio_danger_low) ?: return
        val radioMedium = view.findViewById<RadioButton>(R.id.radio_danger_medium) ?: return
        val radioHigh = view.findViewById<RadioButton>(R.id.radio_danger_high) ?: return
        val resetDangerButton = view.findViewById<Button>(R.id.button_reset_danger) ?: return

        val args = arguments
        val nameResId = args?.getInt("ghostNameResId") ?: 0
        val descriptionResId = args?.getInt("ghostDescriptionResId") ?: 0

        if (nameResId == 0 || descriptionResId == 0) {
            nameView.text = getString(R.string.app_name)
            descriptionView.text = getString(R.string.ghost_detail_missing_data_fallback)
            evidenceView.text = ""
            behaviorsView.text = ""
            return
        }

        nameView.text = getString(nameResId)
        descriptionView.text = getString(descriptionResId)

        val ghost = GhostRepository.ghosts.firstOrNull { it.nameResId == nameResId }

        if (ghost != null) {
            val ctx = requireContext()

            // Evidence
            val allEvidence = if (ghost.extraEvidence.isEmpty()) {
                ghost.evidences
            } else {
                ghost.evidences + ghost.extraEvidence
            }

            val evidenceLabel = allEvidence.joinToString(" / ") { ev ->
                ctx.getString(ev.labelResId)
            }
            evidenceView.text = getString(
                R.string.ghost_row_evidence_format,
                evidenceLabel
            )

            // Behaviors
            if (ghost.possibleBehaviors.isEmpty()) {
                behaviorsView.text = getString(R.string.ghost_row_no_known_behavior)
            } else {
                val behaviorLabel = ghost.possibleBehaviors.joinToString(" • ") { tag ->
                    ctx.getString(tag.labelResId)
                }
                behaviorsView.text = getString(
                    R.string.ghost_row_behavior_format,
                    behaviorLabel
                )
            }

            // 🔧 Danger customization: set initial selection
            val effectiveLevel = DangerSettingsStore.getEffectiveDangerLevel(ctx, ghost)
            when (effectiveLevel) {
                DangerLevel.LOW -> radioLow.isChecked = true
                DangerLevel.MEDIUM -> radioMedium.isChecked = true
                DangerLevel.HIGH -> radioHigh.isChecked = true
            }

            // Update override when user changes selection
            dangerGroup.setOnCheckedChangeListener { _, checkedId ->
                val newLevel = when (checkedId) {
                    R.id.radio_danger_low -> DangerLevel.LOW
                    R.id.radio_danger_medium -> DangerLevel.MEDIUM
                    R.id.radio_danger_high -> DangerLevel.HIGH
                    else -> null
                }
                DangerSettingsStore.setDangerLevel(ctx, ghost, newLevel)
            }

            // Reset button → clear override and go back to default
            resetDangerButton.setOnClickListener {
                DangerSettingsStore.setDangerLevel(ctx, ghost, null)
                // Re-apply default selection
                when (ghost.dangerLevel) {
                    DangerLevel.LOW -> radioLow.isChecked = true
                    DangerLevel.MEDIUM -> radioMedium.isChecked = true
                    DangerLevel.HIGH -> radioHigh.isChecked = true
                }
            }

        } else {
            evidenceView.text = ""
            behaviorsView.text = ""
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
