package com.phasmofriend.app.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.phasmofriend.app.R

enum class DangerLevel(
    @StringRes val labelResId: Int,
    @ColorRes val colorResId: Int
) {
    LOW(R.string.danger_low, R.color.danger_low_bg),
    MEDIUM(R.string.danger_medium, R.color.danger_medium_bg),
    HIGH(R.string.danger_high, R.color.danger_high_bg)
}
