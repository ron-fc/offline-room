package com.nemesis.offlinefroom.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CharacterResponseDto(
    @SerializedName("info")
    val info: InfoDto,
    @SerializedName("results")
    val results: List<CharacterDto>
)

data class InfoDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("prev")
    val prev: String?
)

data class CharacterDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("origin")
    val origin: LocationDto,
    @SerializedName("location")
    val location: LocationDto,
    @SerializedName("image")
    val image: String
)

data class LocationDto(
    @SerializedName("name")
    val name: String
)