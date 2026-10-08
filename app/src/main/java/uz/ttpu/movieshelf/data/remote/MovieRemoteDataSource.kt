package uz.ttpu.movieshelf.data.remote

data class MovieDto(
    val id: Int,
    val title: String,
    val releaseYear: Int,
    val score: Double,
)

interface MovieRemoteDataSource {
    suspend fun fetchMovies(): List<MovieDto>
}