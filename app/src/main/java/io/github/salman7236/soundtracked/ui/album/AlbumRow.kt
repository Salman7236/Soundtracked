package io.github.salman7236.soundtracked.ui.album

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.salman7236.soundtracked.domain.Album
import io.github.salman7236.soundtracked.domain.PrimaryType
import io.github.salman7236.soundtracked.domain.SecondaryType
import io.github.salman7236.soundtracked.ui.theme.SoundtrackedTheme
import java.time.LocalDate

@Composable
fun AlbumRow(
    item: AlbumListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Placeholder until Coil's AsyncImage is added
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.album.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.artistName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.album.subtitle(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun Album.subtitle(): String = buildList {
    primaryType?.let { add(it.label()) }
    secondaryTypes.forEach { add(it.label()) }
    releaseDate?.let { add(it.year.toString()) }
}.joinToString(" · ")

private fun PrimaryType.label(): String = when (this) {
    PrimaryType.ALBUM -> "Album"
    PrimaryType.SINGLE -> "Single"
    PrimaryType.EP -> "EP"
    PrimaryType.BROADCAST -> "Broadcast"
    PrimaryType.OTHER -> "Other"
}

private fun SecondaryType.label(): String = when (this) {
    SecondaryType.COMPILATION -> "Compilation"
    SecondaryType.SOUNDTRACK -> "Soundtrack"
    SecondaryType.SPOKENWORD -> "Spoken word"
    SecondaryType.INTERVIEW -> "Interview"
    SecondaryType.AUDIOBOOK -> "Audiobook"
    SecondaryType.AUDIO_DRAMA -> "Audio drama"
    SecondaryType.LIVE -> "Live"
    SecondaryType.REMIX -> "Remix"
    SecondaryType.DJ_MIX -> "DJ-mix"
    SecondaryType.MIXTAPE -> "Mixtape"
    SecondaryType.DEMO -> "Demo"
    SecondaryType.FIELD_RECORDING -> "Field recording"
}

@Preview(showBackground = true)
@Composable
private fun AlbumRowPreview() {
    SoundtrackedTheme {
        Column {
            AlbumRow(
                item = AlbumListItem(
                    album = Album(
                        id = "1",
                        musicBrainzId = "mbid-1",
                        title = "OK Computer",
                        artistId = "a1",
                        primaryType = PrimaryType.ALBUM,
                        secondaryTypes = emptyList(),
                        releaseDate = LocalDate.of(1997, 6, 16),
                        imageUrl = null,
                    ),
                    artistName = "Radiohead",
                ),
                onClick = {},
            )
            AlbumRow(
                item = AlbumListItem(
                    album = Album(
                        id = "2",
                        musicBrainzId = "mbid-2",
                        title = "An Extremely Long Album Title That Should Get Truncated Nicely",
                        artistId = "a2",
                        primaryType = PrimaryType.ALBUM,
                        secondaryTypes = listOf(SecondaryType.LIVE),
                        releaseDate = null,
                        imageUrl = null,
                    ),
                    artistName = "Some Artist With A Long Name",
                ),
                onClick = {},
            )
        }
    }
}