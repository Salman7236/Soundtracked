package io.github.salman7236.soundtracked.domain

data class Artist(
    val id: String,
    val musicBrainzId: String,
    val name: String,
    val imageUrl: String?,
)