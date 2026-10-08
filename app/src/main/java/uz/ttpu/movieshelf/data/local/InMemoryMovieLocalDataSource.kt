package uz.ttpu.movieshelf.data.local

import uz.ttpu.movieshelf.data.remote.MovieDto

class InMemoryMovieLocalDataSource : MovieLocalDataSource {

    private var cached: List<MovieDto>? = null
    private val favorites = mutableSetOf<Int>()

    override suspend fun getCachedMovies(): List<MovieDto>? = cached

    override suspend fun saveMovies(movies: List<MovieDto>) {
        cached = movies
    }

    override suspend fun getFavoriteIds(): Set<Int> = favorites.toSet()

    override suspend fun setFavorite(id: Int, favorite: Boolean) {
        if (favorite) favorites.add(id) else favorites.remove(id)
    }
}