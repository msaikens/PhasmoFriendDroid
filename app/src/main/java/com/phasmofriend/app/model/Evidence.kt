package com.phasmofriend.app.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.phasmofriend.app.R

enum class Evidence(
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int,
    /** Plain English name for domain-layer text (deduction reasons, next-best-test copy) that can't resolve localized string resources. UI display still uses [labelResId]. */
    val shortName: String
) {
    EMF5(
        R.string.evidence_emf5,
        R.drawable.ic_evidence_emf5,
        "EMF Level 5"
    ),
    SPIRIT_BOX(
        R.string.evidence_spirit_box,
        R.drawable.ic_evidence_spirit_box,
        "Spirit Box"
    ),
    UV(
        R.string.evidence_fingerprints,
        R.drawable.ic_evidence_fingerprints,
        "UV"
    ),
    GHOST_WRITING(
        R.string.evidence_ghost_writing,
        R.drawable.ic_evidence_ghost_writing,
        "Ghost Writing"
    ),
    DOTS(
        R.string.evidence_dots,
        R.drawable.ic_evidence_dots,
        "D.O.T.S. Projector"
    ),
    GHOST_ORB(
        R.string.evidence_ghost_orbs,
        R.drawable.ic_evidence_ghost_orbs,
        "Ghost Orb"
    ),
    FREEZING_TEMPS(
        R.string.evidence_freezing_temps,
        R.drawable.ic_evidence_freezing_temps,
        "Freezing Temps"
    )
}
