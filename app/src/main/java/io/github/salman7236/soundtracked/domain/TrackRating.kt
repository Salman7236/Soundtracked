package io.github.salman7236.soundtracked.domain

import java.time.Instant

data class TrackRating(
    val id: String,
    val userId: String,
    val recordingMusicBrainzId: String,
    val value: Int?,
    val createdAt: Instant,
)