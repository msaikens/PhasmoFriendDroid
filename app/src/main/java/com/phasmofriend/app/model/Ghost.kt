package com.phasmofriend.app.model

import androidx.annotation.StringRes

data class Ghost(
    @StringRes val nameResId: Int,
    val evidences: Set<Evidence>,
    val extraEvidence: Set<Evidence> = emptySet(),
    val possibleBehaviors: Set<BehaviorTag> = emptySet(),
    @StringRes val descriptionResId: Int,
    val dangerLevel: DangerLevel
)
