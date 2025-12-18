package com.phasmofriend.app.ui.notifications

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.phasmofriend.app.R
import com.phasmofriend.app.model.BehaviorTag
import com.phasmofriend.app.ui.shared.InvestigationViewModel
import java.util.Locale

class BehaviorFragment : Fragment() {

    private val viewModel: InvestigationViewModel by activityViewModels()

    private lateinit var containerLayout: LinearLayout
    private lateinit var searchField: EditText

    // Sorted list of all behaviors
    private lateinit var allTags: List<BehaviorTag>

    // One row per behavior
    private data class BehaviorRow(
        val tag: BehaviorTag,
        val rowView: View,
        val checkBox: CheckBox
    )

    private val rows: MutableList<BehaviorRow> = mutableListOf()

    companion object {
        // UI selection truth for behavior checkboxes
        private val behaviorSelection: MutableSet<BehaviorTag> = mutableSetOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_behavior, container, false)

        containerLayout = view.findViewById(R.id.behavior_checkbox_container)
        // TextInputEditText behaves as EditText for our purposes
        searchField = view.findViewById(R.id.behavior_search)

        val ctx = requireContext()

        // Build sorted list once
        allTags = BehaviorTag.entries
            .sortedBy { tag ->
                ctx.getString(tag.labelResId).lowercase(Locale.getDefault())
            }

        // Sync our local selection snapshot with whatever the ViewModel currently has
        behaviorSelection.clear()
        behaviorSelection.addAll(viewModel.selectedBehaviors.value.orEmpty())

        // Build all rows once
        buildAllRows()

        // Filter by hiding/showing rows only (no reinflation)
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { /* no-op */ }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) { /* no-op */ }

            override fun afterTextChanged(s: Editable?) {
                val filter = s?.toString().orEmpty()
                applyFilter(filter)
            }
        })

        return view
    }

    private fun buildAllRows() {
        containerLayout.removeAllViews()
        rows.clear()

        val inflater = LayoutInflater.from(requireContext())

        allTags.forEach { tag ->
            val row = inflater.inflate(
                R.layout.item_behavior_option,
                containerLayout,
                false
            )
            val checkBox = row.findViewById<CheckBox>(R.id.checkbox_behavior)

            checkBox.text = getString(tag.labelResId)

            // Initial visual state from our local selection
            checkBox.isChecked = behaviorSelection.contains(tag)

            // Single toggle handler shared by row + checkbox
            val toggle: (View) -> Unit = {
                val newChecked = !behaviorSelection.contains(tag)
                behaviorSelection.run {
                    if (newChecked) add(tag) else remove(tag)
                }
                checkBox.isChecked = newChecked
                viewModel.setBehaviorChecked(tag, newChecked)
            }

            // Click anywhere on the row, or directly on the checkbox, to toggle
            row.setOnClickListener(toggle)
            checkBox.setOnClickListener(toggle)

            val behaviorRow = BehaviorRow(tag, row, checkBox)
            rows += behaviorRow
            containerLayout.addView(row)
        }
    }

    /**
     * Filter rows by label text without recreating them.
     */
    private fun applyFilter(rawFilter: String) {
        val normalized = rawFilter.trim().lowercase(Locale.getDefault())

        if (normalized.isEmpty()) {
            rows.forEach { row ->
                row.rowView.visibility = View.VISIBLE
            }
            return
        }

        rows.forEach { row ->
            val label = requireContext()
                .getString(row.tag.labelResId)
                .lowercase(Locale.getDefault())

            row.rowView.visibility =
                if (label.contains(normalized)) View.VISIBLE else View.GONE
        }
    }

    /**
     * Called from MainActivity when "Clear All" is tapped.
     * MainActivity has already called investigationViewModel.clearAll(),
     * so here we only clear our UI + local store.
     */
    fun clearSelections() {
        behaviorSelection.clear()

        rows.forEach { row ->
            row.checkBox.isChecked = false
        }
    }
}
