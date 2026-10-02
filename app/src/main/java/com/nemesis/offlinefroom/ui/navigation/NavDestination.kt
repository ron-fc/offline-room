package com.nemesis.offlinefroom.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class DetailRoute(val characterId: Int)