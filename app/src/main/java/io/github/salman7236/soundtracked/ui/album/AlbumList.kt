package io.github.salman7236.soundtracked.ui.album

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.salman7236.soundtracked.domain.Album
import io.github.salman7236.soundtracked.domain.PrimaryType
import io.github.salman7236.soundtracked.domain.SecondaryType
import io.github.salman7236.soundtracked.ui.theme.SoundtrackedTheme
import java.time.LocalDate

@Composable
fun AlbumList(
    items: List<AlbumListItem>,
    onAlbumClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(items, key = { it.album.id }) { item ->
            AlbumRow(
                item = item,
                onClick = { onAlbumClick(item.album.id) },
            )
        }
    }
}

private fun sampleItem(
    id: String,
    title: String,
    artistName: String,
    secondaryTypes: List<SecondaryType> = emptyList(),
    releaseDate: LocalDate? = null,
) = AlbumListItem(
    album = Album(
        id = id,
        musicBrainzId = "mbid-$id",
        title = title,
        artistId = "artist-$id",
        primaryType = PrimaryType.ALBUM,
        secondaryTypes = secondaryTypes,
        releaseDate = releaseDate,
        imageUrl = null,
    ),
    artistName = artistName,
)

@Preview(showBackground = true,  heightDp = 400)
@Composable
private fun AlbumListPreview() {
    SoundtrackedTheme {
        AlbumList(
            items = listOf(
                sampleItem("1", "OK Computer", "Radiohead", releaseDate = LocalDate.of(1997, 6, 16)),
                sampleItem("2", "Kid A", "Radiohead", releaseDate = LocalDate.of(2000, 10, 2)),
                sampleItem("3", "Blonde", "Frank Ocean", releaseDate = LocalDate.of(2016, 8, 20)),
                sampleItem("4", "Live at Massey Hall", "Neil Young", listOf(SecondaryType.LIVE), LocalDate.of(2007, 3, 6)),
                sampleItem("5", "A Title That Is Long Enough To Get Truncated In The Row", "Some Artist"),
                sampleItem("6", "Currents", "Tame Impala", releaseDate = LocalDate.of(2015, 7, 17)),
                sampleItem("7", "To Pimp a Butterfly", "Kendrick Lamar", releaseDate = LocalDate.of(2015, 3, 15)),
                sampleItem("8", "Random Access Memories", "Daft Punk", releaseDate = LocalDate.of(2013, 5, 17)),
            ),
            onAlbumClick = {},
        )
    }
}