package io.github.salman7236.soundtracked

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.salman7236.soundtracked.data.repository.FakeAlbumRepository
import io.github.salman7236.soundtracked.ui.album.AlbumListScreen
import io.github.salman7236.soundtracked.ui.album.AlbumListViewModel
import io.github.salman7236.soundtracked.ui.theme.SoundtrackedTheme

class MainActivity : ComponentActivity() {

    private val viewModel: AlbumListViewModel by viewModels {
        viewModelFactory {
            initializer { AlbumListViewModel(FakeAlbumRepository()) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoundtrackedTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AlbumListScreen(
                        state = uiState,
                        onAlbumClick = {},
                        onRetry = viewModel::load,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}