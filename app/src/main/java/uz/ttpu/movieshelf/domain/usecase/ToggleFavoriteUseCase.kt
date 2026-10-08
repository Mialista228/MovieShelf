package uz.ttpu.movieshelf.domain.usecase

import uz.ttpu.movieshelf.data.repository.MovieRepositoryImpl

class ToggleFavoriteUseCase(
    private val repository: MovieRepositoryImpl
) {
    suspend operator fun invoke(movieId: Int): Boolean {
        return repository.toggleFavorite(movieId)
    }
}