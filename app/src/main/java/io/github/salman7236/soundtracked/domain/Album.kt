package io.github.salman7236.soundtracked.domain

import java.time.LocalDate

data class Album(
    val id: String,
    val musicBrainzId: String,
    val title: String,
    val artistId: String,
    val primaryType: PrimaryType?,
    val secondaryTypes: List<SecondaryType>,
    val releaseDate: LocalDate?,
    val imageUrl: String?,
)