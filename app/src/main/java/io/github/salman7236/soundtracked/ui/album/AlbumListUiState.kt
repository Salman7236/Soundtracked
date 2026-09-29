package io.github.salman7236.soundtracked.ui.album

sealed interface AlbumListUiState {
    data object Loading : AlbumListUiState
    data class Success(val items: List<AlbumListItem>) : AlbumListUiState
    data class Error(val message: String) : AlbumListUiState
}