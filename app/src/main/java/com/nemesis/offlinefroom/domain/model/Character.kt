#!/usr/bin/env kotlin

package com.nemesis.offlinefroom.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val imageUrl: String
)