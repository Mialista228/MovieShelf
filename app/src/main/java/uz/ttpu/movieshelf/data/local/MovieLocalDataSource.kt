package uz.ttpu.movieshelf.data.local

import uz.ttpu.movieshelf.data.remote.MovieDto

interface MovieLocalDataSource {
    suspend fun getCachedMovies(): List<MovieDto>? // null = nothing cached yet
    suspend fun saveMovies(movies: List<MovieDto>)
    suspend fun getFavoriteIds(): Set<Int>
    suspend fun setFavorite(id: Int, favorite: Boolean)
}