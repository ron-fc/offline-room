package com.nemesis.offlinefroom.data.remote.api

import com.nemesis.offlinefroom.data.remote.dto.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): CharacterResponseDto
}