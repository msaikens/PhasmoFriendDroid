// app/src/main/java/com/phasmofriend/app/ui/dashboard/GhostCandidateAdapter.kt
package com.phasmofriend.app.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.phasmofriend.app.R
import com.phasmofriend.app.model.DangerLevel
import com.phasmofriend.app.model.DangerSettingsStore
import com.phasmofriend.app.model.Evidence
import com.phasmofriend.app.model.Ghost

class GhostCandidateAdapter(
    private val onItemClick: (Ghost) -> Unit
) : ListAdapter<Ghost, GhostCandidateAdapter.GhostViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GhostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ghost_candidate, parent, false)
        return GhostViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: GhostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class GhostViewHolder(
        itemView: View,
        private val onItemClick: (Ghost) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val card: MaterialCardView =
            itemView.findViewById(R.id.cardGhost) as MaterialCardView

        private val nameText: TextView =
            itemView.findViewById(R.id.ghost_name)

        private val descriptionText: TextView =
            itemView.findViewById(R.id.ghost_description)

        private val dangerChip: TextView =
            itemView.findViewById(R.id.ghost_danger_chip)

        private val evidenceRow: LinearLayout =
            itemView.findViewById(R.id.evidence_icon_row)

        fun bind(ghost: Ghost) {
            val ctx = itemView.context

            // 👻 Name
            nameText.text = ctx.getString(ghost.nameResId)

            // 💀 Effective danger (user overrides via DangerSettingsStore)
            val danger: DangerLevel = DangerSettingsStore.getEffectiveDangerLevel(ctx, ghost)
            val dangerLabel = ctx.getString(danger.labelResId)
            val dangerColor = ContextCompat.getColor(ctx, danger.colorResId)

            // Short description
            val ghostDescription = ctx.getString(ghost.descriptionResId)
            descriptionText.text = ghostDescription

            // Danger chip
            dangerChip.text = dangerLabel
            dangerChip.background?.mutate()?.setTint(dangerColor)

            // Card border tint by danger
            card.strokeColor = dangerColor

            // 🔍 Evidence icons row
            evidenceRow.removeAllViews()

            val allEvidence: List<Evidence> =
                (ghost.evidences + ghost.extraEvidence).toList()

            if (allEvidence.isEmpty()) return

            val sizePx =
                ctx.resources.getDimensionPixelSize(R.dimen.evidence_icon_size)
            val spacingPx =
                ctx.resources.getDimensionPixelSize(R.dimen.evidence_icon_spacing)

            allEvidence.forEach { ev ->
                val iconView = ImageView(ctx).apply {
                    setImageResource(ev.iconResId)
                    contentDescription = ctx.getString(
                        R.string.evidence_icon_content_desc
                    )

                    layoutParams = LinearLayout.LayoutParams(sizePx, sizePx).apply {
                        rightMargin = spacingPx
                    }

                    // Let the drawable’s own colors show; remove this if you liked the white tint.
                    imageTintList = null
                }

                evidenceRow.addView(iconView)
            }

            // Click → navigate
            itemView.setOnClickListener {
                onItemClick(ghost)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Ghost>() {
        override fun areItemsTheSame(oldItem: Ghost, newItem: Ghost): Boolean {
            // nameResId uniquely identifies ghost type
            return oldItem.nameResId == newItem.nameResId
        }

        override fun areContentsTheSame(oldItem: Ghost, newItem: Ghost): Boolean {
            return oldItem == newItem
        }
    }
}
