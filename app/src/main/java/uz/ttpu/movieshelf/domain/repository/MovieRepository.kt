package uz.ttpu.movieshelf.domain.repository

import uz.ttpu.movieshelf.domain.model.MoviesResult

interface MovieRepository {
    suspend fun getMovies(): MoviesResult

    suspend fun toggleFavorite(movieId: Int): Boolean
}