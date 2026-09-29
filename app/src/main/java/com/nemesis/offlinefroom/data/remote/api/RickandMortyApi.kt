
package com.nemesis.offlinefroom.data.remote.api

import com.nemesis.offlinefroom.data.remote.dto.CharacterResponseDto
import retrofit2.http.GET

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(): CharacterResponseDto
}