package io.github.salman7236.soundtracked.domain

import java.time.LocalDate
import java.time.LocalTime
import java.time.Instant

data class DiaryEntry (
    val id: String,
    val albumId: String,
    val releaseId: String?,
    val note: String?,
    val listenedDate: LocalDate,
    val listenedTime: LocalTime?,
    val createdAt: Instant,
    val userId: String,
)