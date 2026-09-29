package io.github.salman7236.soundtracked.ui.album

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.salman7236.soundtracked.ui.theme.SoundtrackedTheme

@Composable
fun AlbumListScreen(
    state: AlbumListUiState,
    onAlbumClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        AlbumListUiState.Loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }

        is AlbumListUiState.Success -> AlbumList(
            items = state.items,
            onAlbumClick = onAlbumClick,
            modifier = modifier,
        )

        is AlbumListUiState.Error -> Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = state.message,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlbumListScreenLoadingPreview() {
    SoundtrackedTheme {
        AlbumListScreen(
            state = AlbumListUiState.Loading,
            onAlbumClick = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlbumListScreenErrorPreview() {
    SoundtrackedTheme {
        AlbumListScreen(
            state = AlbumListUiState.Error("Couldn't load albums"),
            onAlbumClick = {},
            onRetry = {},
        )
    }
}