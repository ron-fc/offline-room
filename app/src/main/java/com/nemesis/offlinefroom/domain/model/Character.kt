package com.nemesis.offlinefroom.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val imageUrl: String,
    val gender: String = "",
    val origin: String = "",
    val lastKnownLocation: String = ""
)