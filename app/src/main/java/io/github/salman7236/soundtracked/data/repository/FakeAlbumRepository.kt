package io.github.salman7236.soundtracked.data.repository

import io.github.salman7236.soundtracked.domain.Album
import io.github.salman7236.soundtracked.domain.Artist
import io.github.salman7236.soundtracked.domain.PrimaryType
import io.github.salman7236.soundtracked.domain.SecondaryType
import kotlinx.coroutines.delay
import java.time.LocalDate

class FakeAlbumRepository : AlbumRepository {

    override suspend fun getAlbums(): List<Album> {
        delay(800) // simulate network so the Loading state is visible
        return albums
    }

    override suspend fun getArtists(): List<Artist> = artists

    private val artists = listOf(
        artist("a1", "Radiohead"),
        artist("a2", "Frank Ocean"),
        artist("a3", "Neil Young"),
        artist("a4", "Tame Impala"),
        artist("a5", "Kendrick Lamar"),
        artist("a6", "Daft Punk"),
    )

    private val albums = listOf(
        album("1", "OK Computer", "a1", LocalDate.of(1997, 6, 16)),
        album("2", "Kid A", "a1", LocalDate.of(2000, 10, 2)),
        album("3", "In Rainbows", "a1", LocalDate.of(2007, 10, 10)),
        album("4", "Blonde", "a2", LocalDate.of(2016, 8, 20)),
        album("5", "Channel Orange", "a2", LocalDate.of(2012, 7, 10)),
        album("6", "Live at Massey Hall", "a3", LocalDate.of(2007, 3, 6), listOf(SecondaryType.LIVE)),
        album("7", "Harvest", "a3", LocalDate.of(1972, 2, 14)),
        album("8", "Currents", "a4", LocalDate.of(2015, 7, 17)),
        album("9", "Lonerism", "a4", LocalDate.of(2012, 10, 5)),
        album("10", "To Pimp a Butterfly", "a5", LocalDate.of(2015, 3, 15)),
        album("11", "good kid, m.A.A.d city", "a5", LocalDate.of(2012, 10, 22)),
        album("12", "Random Access Memories", "a6", LocalDate.of(2013, 5, 17)),
        album("13", "Discovery", "a6", LocalDate.of(2001, 3, 12)),
        album("14", "A Deliberately Long Title To Test Truncation In The Album Row", "a6", null),
    )

    private fun artist(id: String, name: String) = Artist(
        id = id,
        musicBrainzId = "mbid-$id",
        name = name,
        imageUrl = null,
    )

    private fun album(
        id: String,
        title: String,
        artistId: String,
        releaseDate: LocalDate?,
        secondaryTypes: List<SecondaryType> = emptyList(),
    ) = Album(
        id = id,
        musicBrainzId = "mbid-$id",
        title = title,
        artistId = artistId,
        primaryType = PrimaryType.ALBUM,
        secondaryTypes = secondaryTypes,
        releaseDate = releaseDate,
        imageUrl = null,
    )
}