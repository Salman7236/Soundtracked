package io.github.salman7236.soundtracked.ui.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.salman7236.soundtracked.data.repository.AlbumRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumListViewModel(
    private val repository: AlbumRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlbumListUiState>(AlbumListUiState.Loading)
    val uiState: StateFlow<AlbumListUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = AlbumListUiState.Loading
        viewModelScope.launch {
            try {
                val albums = repository.getAlbums()
                val artistsById = repository.getArtists().associateBy { it.id }
                val items = albums.map { album ->
                    AlbumListItem(
                        album = album,
                        artistName = artistsById[album.artistId]?.name ?: "Unknown artist",
                    )
                }
                _uiState.value = AlbumListUiState.Success(items)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = AlbumListUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}