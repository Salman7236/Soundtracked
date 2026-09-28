package io.github.salman7236.soundtracked.domain

import java.time.Instant

data class AlbumRating(
    val id: String,
    val userId: String,
    val albumId: String,
    val value: Int?,
    val createdAt: Instant,
)