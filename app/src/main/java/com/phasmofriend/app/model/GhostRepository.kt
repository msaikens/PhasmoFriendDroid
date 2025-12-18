package com.phasmofriend.app.model

import com.phasmofriend.app.R

object GhostRepository {

    val ghosts: List<Ghost> = listOf(

        Ghost(
            nameResId = R.string.ghost_spirit_name,
            evidences = setOf(
                Evidence.EMF5,
                Evidence.SPIRIT_BOX,
                Evidence.GHOST_WRITING
            ),
            possibleBehaviors = setOf(
                BehaviorTag.LATE_HUNTER
            ),
            descriptionResId = R.string.ghost_spirit_desc,
            dangerLevel = DangerLevel.LOW
        ),

        Ghost(
            nameResId = R.string.ghost_wraith_name,
            evidences = setOf(
                Evidence.EMF5,
                Evidence.SPIRIT_BOX,
                Evidence.DOTS
            ),
            possibleBehaviors = setOf(
                BehaviorTag.ROAMER,
                BehaviorTag.AVOIDS_SALT
            ),
            descriptionResId = R.string.ghost_wraith_desc,
            dangerLevel = DangerLevel.LOW
        ),

        Ghost(
            nameResId = R.string.ghost_phantom_name,
            evidences = setOf(
                Evidence.DOTS,
                Evidence.UV,
                Evidence.SPIRIT_BOX
            ),
            possibleBehaviors = setOf(
                BehaviorTag.SANITY_DRAIN_STRONG,
                BehaviorTag.ROAMER,
                BehaviorTag.SLOW_BLINK
            ),
            descriptionResId = R.string.ghost_phantom_desc,
            dangerLevel = DangerLevel.MEDIUM
        ),

        Ghost(
            nameResId = R.string.ghost_poltergeist_name,
            evidences = setOf(
                Evidence.GHOST_WRITING,
                Evidence.UV,
                Evidence.SPIRIT_BOX
            ),
            possibleBehaviors = setOf(
                BehaviorTag.SANITY_DRAIN_STRONG,
                BehaviorTag.THROWS_PILES_OR_MULTIPLE_AT_ONCE
            ),
            descriptionResId = R.string.ghost_poltergeist_desc,
            dangerLevel = DangerLevel.MEDIUM
        ),

        Ghost(
            nameResId = R.string.ghost_banshee_name,
            evidences = setOf(
                Evidence.DOTS,
                Evidence.GHOST_ORB,
                Evidence.UV
            ),
            possibleBehaviors = setOf(
                BehaviorTag.ROAMER,
                BehaviorTag.SANITY_DRAIN_STRONG,
                BehaviorTag.SCREAMS_IN_AUDIO_OR_PARABOLIC
            ),
            descriptionResId = R.string.ghost_banshee_desc,
            dangerLevel = DangerLevel.MEDIUM
        ),

        Ghost(
            nameResId = R.string.ghost_jinn_name,
            evidences = setOf(
                Evidence.EMF5,
                Evidence.UV,
                Evidence.FREEZING_TEMPS
            ),
            possibleBehaviors = setOf(
                BehaviorTag.LINE_OF_SIGHT_SPEED_UP
            ),
            descriptionResId = R.string.ghost_jinn_desc,
            dangerLevel = DangerLevel.HIGH
        ),

        Ghost(
            nameResId = R.string.ghost_mare_name,
            evidences = setOf(
                Evidence.GHOST_WRITING,
                Evidence.GHOST_ORB,
                Evidence.SPIRIT_BOX
            ),
            possibleBehaviors = setOf(
                BehaviorTag.EARLY_HUNTER
            ),
            descriptionResId = R.string.ghost_mare_desc,
            dangerLevel = DangerLevel.HIGH
        ),

        Ghost(
            nameResId = R.string.ghost_revenant_name,
            evidences = setOf(
                Evidence.GHOST_WRITING,
                Evidence.GHOST_ORB,
                Evidence.FREEZING_TEMPS
            ),
            possibleBehaviors = setOf(
                BehaviorTag.SLOW_HUNT,
                BehaviorTag.LINE_OF_SIGHT_SPEED_UP,
                BehaviorTag.SLOW_WHEN_HIDDEN_FAST_WHEN_VISIBLE
            ),
            descriptionResId = R.string.ghost_revenant_desc,
            dangerLevel = DangerLevel.HIGH
        ),

        Ghost(
            nameResId = R.string.ghost_shade_name,
            evidences = setOf(
                Evidence.GHOST_WRITING,
                Evidence.EMF5,
                Evidence.FREEZING_TEMPS
            ),
            possibleBehaviors = setOf(
                BehaviorTag.SHY,
                BehaviorTag.CALM_WHEN_PLAYER_IN_ROOM
            ),
            descriptionResId = R.string.ghost_shade_desc,
            dangerLevel = DangerLevel.LOW
        ),

        Ghost(
            nameResId = R.string.ghost_demon_name,
            evidences = setOf(
                Evidence.GHOST_WRITING,
                Evidence.UV,
                Evidence.FREEZING_TEMPS
            ),
            possibleBehaviors = setOf(
                BehaviorTag.EARLY_HUNTER
            ),
            descriptionResId = R.string.ghost_demon_desc,
            dangerLevel = DangerLevel.HIGH
        ),

        Ghost(
            nameResId = R.string.ghost_yurei_name,
            evidences = setOf(
                Evidence.DOTS,
                Evidence.GHOST_ORB,
                Evidence.FREEZING_TEMPS
            ),
            possibleBehaviors = setOf(
                BehaviorTag.SANITY_DRAIN_STRONG
            ),
            descriptionResId = R.string.ghost_yurei_desc,
            dangerLevel = DangerLevel.MEDIUM
        ),

        Ghost(
            nameResId = R.string.ghost_oni_name,
            evidences = setOf(
                Evidence.DOTS,
                Evidence.EMF5,
                Evidence.FREEZING_TEMPS
            ),
            possibleBehaviors = setOf(
                BehaviorTag.SANITY_DRAIN_STRONG,
                BehaviorTag.CANNOT_MIST_FORM,
                BehaviorTag.FAST_BLINK
            ),
            descriptionResId = R.string.ghost_oni_desc,
            dangerLevel = DangerLevel.MEDIUM
        ),

        Ghost(
            nameResId = R.string.ghost_hantu_name,
            evidences = setOf(
                Evidence.GHOST_ORB,
                Evidence.UV,
                Evidence.FREEZING_TEMPS
            ),
            possibleBehaviors = setOf(
                BehaviorTag.FAST_HUNT,
                BehaviorTag.SLOW_HUNT,
                BehaviorTag.VISIBLE_BREATH
            ),
            descriptionResId = R.string.ghost_hantu_desc,
            dangerLevel = DangerLevel.HIGH
        ),

        Ghost(
            nameResId = R.string.ghost_yokai_name,
            evidences = setOf(
                Evidence.DOTS,
                Evidence.GHOST_ORB,
                Evidence.SPIRIT_BOX
            ),
            possibleBehaviors = setOf(
                BehaviorTag.EARLY_HUNTER,
                BehaviorTag.HUNTS_WHEN_YOU_TALK
            ),
            descriptionResId = R.string.ghost_yokai_desc,
            dangerLevel = DangerLevel.MEDIUM
        ),

        Ghost(
            nameResId = R.string.ghost_myling_name,
            evidences = setOf(
                Evidence.GHOST_WRITING,
                Evidence.EMF5,
                Evidence.UV
            ),
            possibleBehaviors = setOf(
                BehaviorTag.SLOW_HUNT,
                BehaviorTag.HARD_TO_HEAR_HUNTING
            ),
            descriptionResId = R.string.ghost_myling_desc,
            dangerLevel = DangerLevel.LOW
        ),

        Ghost(
            nameResId = R.string.ghost_onryo_name,
            evidences = setOf(
                Evidence.GHOST_ORB,
                Evidence.FREEZING_TEMPS,
                Evidence.SPIRIT_BOX
            ),
            possibleBehaviors = setOf(
                BehaviorTag.EARLY_HUNTER,
                BehaviorTag.EXTINGUISHED_FLAME_CAUSES_HUNT
            ),
            descriptionResId = R.string.ghost_onryo_desc,
            dangerLevel = DangerLevel.MEDIUM
        ),

        Ghost(
            nameResId = R.string.ghost_twins_name,
            evidences = setOf(
                Evidence.EMF5,
                Evidence.FREEZING_TEMPS,
                Evidence.SPIRIT_BOX
            ),
            possibleBehaviors = setOf(
                BehaviorTag.ROAMER,
                BehaviorTag.ACTIVITY_IN_MULTIPLE_PLACES_AT_ONCE
            ),
            descriptionResId = R.string.ghost_twins_desc,
            dangerLevel = DangerLevel.MEDIUM
        ),

        Ghost(
            nameResId = R.string.ghost_raiju_name,
            evidences = setOf(
                Evidence.DOTS,
                Evidence.EMF5,
                Evidence.GHOST_ORB
            ),
            possibleBehaviors = setOf(
                BehaviorTag.FAST_HUNT,
                BehaviorTag.FASTER_NEAR_ELECTRONICS
            ),
            descriptionResId = R.string.ghost_raiju_desc,
            dangerLevel = DangerLevel.HIGH
        ),

        Ghost(
            nameResId = R.string.ghost_obake_name,
            evidences = setOf(
                Evidence.EMF5,
                Evidence.GHOST_ORB,
                Evidence.UV
            ),
            possibleBehaviors = setOf(
                BehaviorTag.SANITY_DRAIN_STRONG,
                BehaviorTag.SHAPESHIFTER,
                BehaviorTag.HANDPRINT_HAS_SIX_FINGERS
            ),
            descriptionResId = R.string.ghost_obake_desc,
            dangerLevel = DangerLevel.MEDIUM
        ),

        Ghost(
            nameResId = R.string.ghost_goryo_name,
            evidences = setOf(
                Evidence.DOTS,
                Evidence.EMF5,
                Evidence.UV
            ),
            possibleBehaviors = setOf(
                BehaviorTag.SHY,
                BehaviorTag.DOTS_ONLY_VISIBLE_THROUGH_CAMERA
            ),
            descriptionResId = R.string.ghost_goryo_desc,
            dangerLevel = DangerLevel.LOW
        ),

        Ghost(
            nameResId = R.string.ghost_moroi_name,
            evidences = setOf(
                Evidence.GHOST_WRITING,
                Evidence.FREEZING_TEMPS,
                Evidence.SPIRIT_BOX
            ),
            possibleBehaviors = setOf(
                BehaviorTag.FAST_HUNT,
                BehaviorTag.SANITY_DRAIN_STRONG
            ),
            descriptionResId = R.string.ghost_moroi_desc,
            dangerLevel = DangerLevel.MEDIUM
        ),

        Ghost(
            nameResId = R.string.ghost_deogen_name,
            evidences = setOf(
                Evidence.DOTS,
                Evidence.GHOST_WRITING,
                Evidence.SPIRIT_BOX
            ),
            possibleBehaviors = setOf(
                BehaviorTag.SLOW_HUNT,
                BehaviorTag.SANITY_DRAIN_STRONG,
                BehaviorTag.SLOWS_DOWN_AS_IT_APPROACHES_PLAYER,
                BehaviorTag.CANNOT_HIDE_FROM_GHOST
            ),
            descriptionResId = R.string.ghost_deogen_desc,
            dangerLevel = DangerLevel.LOW
        ),

        Ghost(
            nameResId = R.string.ghost_thaye_name,
            evidences = setOf(
                Evidence.DOTS,
                Evidence.GHOST_WRITING,
                Evidence.GHOST_ORB
            ),
            possibleBehaviors = setOf(
                BehaviorTag.EARLY_HUNTER,
                BehaviorTag.LATE_HUNTER,
                BehaviorTag.HUNT_MOVEMENT_SPEED_DECREASES_OVER_TIME
            ),
            descriptionResId = R.string.ghost_thaye_desc,
            dangerLevel = DangerLevel.MEDIUM
        ),

        // The Mimic – special case with extra fake Orbs
        Ghost(
            nameResId = R.string.ghost_mimic_name,
            evidences = setOf(
                Evidence.UV,
                Evidence.FREEZING_TEMPS,
                Evidence.SPIRIT_BOX
            ),
            extraEvidence = setOf(
                Evidence.GHOST_ORB
            ),
            possibleBehaviors = setOf(
                BehaviorTag.ROAMER
            ),
            descriptionResId = R.string.ghost_mimic_desc,
            dangerLevel = DangerLevel.HIGH
        )
    )
}
