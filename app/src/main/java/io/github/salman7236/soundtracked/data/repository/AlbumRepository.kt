package io.github.salman7236.soundtracked.data.repository

import io.github.salman7236.soundtracked.domain.Album
import io.github.salman7236.soundtracked.domain.Artist

interface AlbumRepository {
    suspend fun getAlbums(): List<Album>
    suspend fun getArtists(): List<Artist>
}