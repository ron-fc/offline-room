package com.nemesis.offlinefroom.data.mapper

import com.nemesis.offlinefroom.data.local.entity.CharacterEntity
import com.nemesis.offlinefroom.data.remote.dto.CharacterDto
import com.nemesis.offlinefroom.domain.model.Character

fun CharacterDto.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    imageUrl = image
)

fun CharacterEntity.toDomain(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    imageUrl = imageUrl
)

fun CharacterDto.toDomain(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    imageUrl = image
)