package io.github.salman7236.soundtracked.domain

import java.time.Instant

data class Review(
    val id: String,
    val userId: String,
    val albumId: String,
    val body: String?,
    val createdAt: Instant,
)