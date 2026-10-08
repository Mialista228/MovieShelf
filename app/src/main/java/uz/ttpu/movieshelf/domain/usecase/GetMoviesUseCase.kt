package uz.ttpu.movieshelf.domain.usecase

import uz.ttpu.movieshelf.data.repository.MovieRepositoryImpl
import uz.ttpu.movieshelf.domain.model.MoviesResult

// ARCHITECTURAL FLAW / DEPENDENCY RULE VIOLATION:
// Right now, the domain layer (GetMoviesUseCase) directly imports and depends on the data layer (MovieRepositoryImpl).
// The Clean Architecture Dependency Rule states that dependencies must always point INWARD toward the domain layer.
// Domain should be the central core and know nothing about specific data implementations.
class GetMoviesUseCase(
    private val repository: MovieRepositoryImpl
) {
    suspend operator fun invoke(): MoviesResult {
        val result = repository.getMovies()
        val sortedMovies = result.movies.sortedWith(
            compareByDescending<uz.ttpu.movieshelf.domain.model.Movie> { it.rating }
                .thenBy { it.title }
        )
        return result.copy(movies = sortedMovies)
    }
}