package com.phasmofriend.app.model

data class Ghost(
    val id: String,
    val name: String,
    val evidences: Set<Evidence>,
    val extraEvidence: Set<Evidence> = emptySet(),
    val possibleBehaviors: Set<Behavior> = emptySet(),
    val description: String,
    val dangerLevel: DangerLevel
)
