package com.phasmofriend.app.model

import androidx.annotation.StringRes
import com.phasmofriend.app.R

enum class BehaviorTag(@StringRes val labelResId: Int) {

    ACTIVITY_IN_MULTIPLE_PLACES_AT_ONCE(R.string.behavior_activity_in_multiple_places_at_once),
    AVOIDS_SALT(R.string.behavior_avoids_salt),
    CALM_WHEN_PLAYER_IN_ROOM(R.string.behavior_calm_when_player_in_room),
    CANNOT_HIDE_FROM_GHOST(R.string.behavior_cannot_hide_from_ghost),
    CANNOT_MIST_FORM(R.string.behavior_cannot_mist_form),
    DOTS_ONLY_VISIBLE_THROUGH_CAMERA(R.string.behavior_dots_only_visible_through_camera),
    EARLY_HUNTER(R.string.behavior_early_hunter),
    EXTINGUISHED_FLAME_CAUSES_HUNT(R.string.behavior_extinguished_flame_causes_hunt),
    FAST_BLINK(R.string.behavior_fast_blink),
    FAST_HUNT(R.string.behavior_fast_hunt),
    FASTER_NEAR_ELECTRONICS(R.string.behavior_faster_near_electronics),
    HARD_TO_HEAR_HUNTING(R.string.behavior_hard_to_hear_hunting),
    HANDPRINT_HAS_SIX_FINGERS(R.string.behavior_handprint_has_six_fingers),
    HUNT_MOVEMENT_SPEED_DECREASES_OVER_TIME(R.string.behavior_hunt_movement_speed_decreases_over_time),
    HUNTS_WHEN_YOU_TALK(R.string.behavior_hunts_when_you_talk),
    LATE_HUNTER(R.string.behavior_late_hunter),
    LINE_OF_SIGHT_SPEED_UP(R.string.behavior_line_of_sight_speed_up),
    ROAMER(R.string.behavior_roamer),
    SANITY_DRAIN_STRONG(R.string.behavior_sanity_drain_strong),
    SANITY_DRAIN_WEAK(R.string.behavior_sanity_drain_weak),
    SCREAMS_IN_AUDIO_OR_PARABOLIC(R.string.behavior_screams_in_audio_or_parabolic),
    SHAPESHIFTER(R.string.behavior_shapeshifter),
    SHY(R.string.behavior_shy),
    SLOW_BLINK(R.string.behavior_slow_blink),
    SLOW_HUNT(R.string.behavior_slow_hunt),
    SLOW_WHEN_HIDDEN_FAST_WHEN_VISIBLE(R.string.behavior_slow_when_hidden_fast_when_visible),
    SLOWS_DOWN_AS_IT_APPROACHES_PLAYER(R.string.behavior_slows_down_as_it_approaches_player),
    THROWS_PILES_OR_MULTIPLE_AT_ONCE(R.string.behavior_throws_piles_or_multiple_at_once),
    VISIBLE_BREATH(R.string.behavior_visible_breath);
}
