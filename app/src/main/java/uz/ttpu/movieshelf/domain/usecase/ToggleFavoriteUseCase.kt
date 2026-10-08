package uz.ttpu.movieshelf.domain.usecase

import uz.ttpu.movieshelf.domain.repository.MovieRepository

class ToggleFavoriteUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Boolean {
        return repository.toggleFavorite(movieId)
    }
}