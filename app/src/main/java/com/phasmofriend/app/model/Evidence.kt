package com.phasmofriend.app.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.phasmofriend.app.R

enum class Evidence(
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int
) {
    EMF5(
        R.string.evidence_emf5,
        R.drawable.ic_evidence_emf5
    ),
    SPIRIT_BOX(
        R.string.evidence_spirit_box,
        R.drawable.ic_evidence_spirit_box
    ),
    UV(
        R.string.evidence_fingerprints,
        R.drawable.ic_evidence_fingerprints
    ),
    GHOST_WRITING(
        R.string.evidence_ghost_writing,
        R.drawable.ic_evidence_ghost_writing
    ),
    DOTS(
        R.string.evidence_dots,
        R.drawable.ic_evidence_dots
    ),
    GHOST_ORB(
        R.string.evidence_ghost_orbs,
        R.drawable.ic_evidence_ghost_orbs
    ),
    FREEZING_TEMPS(
        R.string.evidence_freezing_temps,
        R.drawable.ic_evidence_freezing_temps
    )
}
