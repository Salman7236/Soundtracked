package io.github.salman7236.soundtracked.domain

import java.time.Instant

data class AlbumFavorite(
    val id: String,
    val userId: String,
    val albumId: String,
    val createdAt: Instant,
)