package com.phasmofriend.app.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Evidence : Route

    @Serializable
    data object Behavior : Route

    @Serializable
    data object Results : Route

    @Serializable
    data class GhostDetail(val ghostId: String) : Route
}
