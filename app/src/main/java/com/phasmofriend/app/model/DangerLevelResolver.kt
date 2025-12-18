package com.phasmofriend.app.model

import com.phasmofriend.app.R

object DangerLevelResolver {

    fun getDefaultDangerLevel(ghost: Ghost): DangerLevel {
        return when (ghost.nameResId) {
            // 🔴 High danger ghosts
            R.string.ghost_demon_name,
            R.string.ghost_revenant_name,
            R.string.ghost_raiju_name,
            R.string.ghost_moroi_name,
            R.string.ghost_deogen_name,
            R.string.ghost_thaye_name,
            R.string.ghost_oni_name,
            R.string.ghost_twins_name -> DangerLevel.HIGH

            // 🟠 Moderate danger ghosts
            R.string.ghost_poltergeist_name,
            R.string.ghost_obambo_name,
            R.string.ghost_banshee_name,
            R.string.ghost_jinn_name,
            R.string.ghost_yokai_name,
            R.string.ghost_hantu_name,
            R.string.ghost_myling_name,
            R.string.ghost_onryo_name,
            R.string.ghost_obake_name,
            R.string.ghost_goryo_name,
            R.string.ghost_mimic_name -> DangerLevel.MEDIUM

            // 🟢 Lower danger (relatively speaking)
            else -> DangerLevel.LOW
        }
    }
}
