package uz.ttpu.movieshelf.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val rating: Double,
    val isFavorite: Boolean = false,
)

data class MoviesResult(
    val movies: List<Movie>,
    val isFromCache: Boolean, // true = the network failed and this is the saved copy
)