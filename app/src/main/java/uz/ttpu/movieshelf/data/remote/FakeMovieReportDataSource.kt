package uz.ttpu.movieshelf.data.remote

import kotlinx.coroutines.delay
import java.io.IOException

class FakeMovieRemoteDataSource : MovieRemoteDataSource {

    @Volatile
    var isOnline: Boolean = true

    override suspend fun fetchMovies(): List<MovieDto> {
        delay(800) // pretend to wait for the network
        if (!isOnline) throw IOException("No connection")
        return listOf(
            MovieDto(1, "The Silk Road Express", 2019, 8.4),
            MovieDto(2, "Midnight in Samarkand", 2021, 7.9),
            MovieDto(3, "Registan Rhapsody", 2018, 8.4),
            MovieDto(4, "Steel Orchard", 2023, 6.8),
            MovieDto(5, "Chorsu Blues", 2020, 7.2),
            MovieDto(6, "Echoes of Khiva", 2022, 9.0),
            MovieDto(7, "Paper Minarets", 2017, 7.9),
            MovieDto(8, "Last Train to Bukhara", 2024, 8.1),
        )
    }
}