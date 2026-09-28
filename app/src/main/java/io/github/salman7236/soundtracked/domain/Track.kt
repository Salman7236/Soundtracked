package io.github.salman7236.soundtracked.domain

data class Track(
    val id: String,
    val musicBrainzId: String,
    val recordingMusicBrainzId: String,
    val releaseId: String,
    val artistId: String,
    val title: String,
    val discNumber: Int,
    val position: Int,
    val durationMs: Int?,
)