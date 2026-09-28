package io.github.salman7236.soundtracked.domain

import java.time.LocalDate

data class Release(
    val id: String,
    val musicBrainzId: String,
    val editionName: String?,
    val albumId: String,
    val format: Format?,
    val country: String?,
    val releaseDate: LocalDate?,
    val imageUrl: String?,
)