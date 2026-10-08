package uz.ttpu.movieshelf.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.ttpu.movieshelf.domain.model.Movie
import uz.ttpu.movieshelf.domain.usecase.GetMoviesUseCase
import uz.ttpu.movieshelf.domain.usecase.ToggleFavoriteUseCase
import java.io.IOException

sealed interface MovieListUiState {
    data object Loading : MovieListUiState
    data class Success(
        val movies: List<Movie>,
        val isFromCache: Boolean,
    ) : MovieListUiState
    data class Error(val message: String) : MovieListUiState
}

class MovieListViewModel(
    private val getMovies: GetMoviesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
    val state: StateFlow<MovieListUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun onRefresh() = load()

    fun onFavoriteClick(movieId: Int) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is MovieListUiState.Success) {
                // Toggle favorite in repository via use case
                val newFavoriteState = toggleFavorite(movieId)

                // Update current state locally without triggering a full network reload
                val updatedMovies = currentState.movies.map { movie ->
                    if (movie.id == movieId) {
                        movie.copy(isFavorite = newFavoriteState)
                    } else {
                        movie
                    }
                }
                _state.value = currentState.copy(movies = updatedMovies)
            }
        }
    }

    private fun load() {
        _state.value = MovieListUiState.Loading
        viewModelScope.launch {
            try {
                val result = getMovies()
                _state.value = MovieListUiState.Success(
                    movies = result.movies,
                    isFromCache = result.isFromCache
                )
            } catch (e: IOException) {
                _state.value = MovieListUiState.Error(
                    message = "Can't load movies. Check your connection and try again."
                )
            }
        }
    }
}