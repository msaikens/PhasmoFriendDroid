package com.phasmofriend.app.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.phasmofriend.app.R
import com.phasmofriend.app.model.BehaviorTag

class BehaviorAdapter(
    private var behaviors: List<BehaviorTag>,
    private val onToggle: (BehaviorTag) -> Unit
) : RecyclerView.Adapter<BehaviorAdapter.BehaviorViewHolder>() {

    // Set of currently selected behaviors, from the ViewModel
    private var selected: Set<BehaviorTag> = emptySet()

    /**
     * Replace the underlying list (used for search / filter).
     */
    fun updateItems(newBehaviors: List<BehaviorTag>) {
        behaviors = newBehaviors
        notifyDataSetChanged()
    }

    /**
     * Update which behaviors are selected.
     * Only rows whose checked state changed will be refreshed.
     */
    fun setSelected(selectedBehaviors: Set<BehaviorTag>) {
        val old = selected
        selected = selectedBehaviors

        behaviors.forEachIndexed { index, tag ->
            val wasSelected = tag in old
            val isSelectedNow = tag in selectedBehaviors
            if (wasSelected != isSelectedNow) {
                notifyItemChanged(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BehaviorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_behavior_option, parent, false)
        return BehaviorViewHolder(view, onToggle)
    }

    override fun onBindViewHolder(holder: BehaviorViewHolder, position: Int) {
        val tag = behaviors[position]
        holder.bind(tag, selected.contains(tag))
    }

    override fun getItemCount(): Int = behaviors.size

    class BehaviorViewHolder(
        itemView: View,
        private val onToggle: (BehaviorTag) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox_behavior)

        fun bind(tag: BehaviorTag, isSelected: Boolean) {
            val ctx = itemView.context

            val label = ctx.getString(tag.labelResId)

            checkBox.setOnCheckedChangeListener(null)
            checkBox.text = label
            checkBox.isChecked = isSelected

            checkBox.setOnCheckedChangeListener { _, _ ->
                // We only signal a toggle; ViewModel owns the source of truth.
                onToggle(tag)
            }
        }
    }
}
