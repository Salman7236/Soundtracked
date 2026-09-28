package io.github.salman7236.soundtracked.domain

data class Rating (
    val id: String,
    val albumRating: String,
    val trackId: String,
    val rating: Int,
    val userId: String,
)