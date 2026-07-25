package com.phasmofriend.app.model;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import com.phasmofriend.app.R;

public enum DangerLevel {
    LOW(
            R.string.danger_low,
            R.color.danger_low_bg
    ),
    MEDIUM(
            R.string.danger_medium,
            R.color.danger_medium_bg
    ),
    HIGH(
            R.string.danger_high,
            R.color.danger_high_bg
    );

    @StringRes
    public final int labelResId;

    @ColorRes
    public final int colorResId;

    DangerLevel(@StringRes int labelResId, @ColorRes int colorResId) {
        this.labelResId = labelResId;
        this.colorResId = colorResId;
    }
}
