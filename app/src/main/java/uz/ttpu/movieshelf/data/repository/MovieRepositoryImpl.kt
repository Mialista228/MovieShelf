package uz.ttpu.movieshelf.data.repository

import kotlinx.coroutines.CancellationException
import uz.ttpu.movieshelf.data.local.MovieLocalDataSource
import uz.ttpu.movieshelf.data.mapper.toDomain
import uz.ttpu.movieshelf.data.remote.MovieRemoteDataSource
import uz.ttpu.movieshelf.domain.model.MoviesResult
import java.io.IOException

class MovieRepositoryImpl(
    private val remote: MovieRemoteDataSource,
    private val local: MovieLocalDataSource,
) {

    suspend fun getMovies(): MoviesResult {
        // Local storage is the single source of truth for favorites
        val favoriteIds = local.getFavoriteIds()

        return try {
            // Fetch fresh data from remote
            val remoteDtos = remote.fetchMovies()
            // Save fresh copy to local cache
            local.saveMovies(remoteDtos)

            val domainMovies = remoteDtos.map { dto ->
                dto.toDomain(isFavorite = favoriteIds.contains(dto.id))
            }
            MoviesResult(movies = domainMovies, isFromCache = false)
        } catch (e: Exception) {
            if (e is CancellationException) throw e // Never swallow CancellationException
            if (e is IOException) {
                val cachedDtos = local.getCachedMovies()
                    ?: throw e // Fallback to cache; if no cache exists, rethrow

                val domainMovies = cachedDtos.map { dto ->
                    dto.toDomain(isFavorite = favoriteIds.contains(dto.id))
                }
                MoviesResult(movies = domainMovies, isFromCache = true)
            } else {
                throw e
            }
        }
    }

    suspend fun toggleFavorite(movieId: Int): Boolean {
        val favoriteIds = local.getFavoriteIds()
        val isCurrentlyFavorite = favoriteIds.contains(movieId)
        val newFavoriteState = !isCurrentlyFavorite

        local.setFavorite(movieId, newFavoriteState)
        return newFavoriteState
    }
}