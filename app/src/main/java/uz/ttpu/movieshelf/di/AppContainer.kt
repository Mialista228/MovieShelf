package uz.ttpu.movieshelf.di

import uz.ttpu.movieshelf.data.local.InMemoryMovieLocalDataSource
import uz.ttpu.movieshelf.data.local.MovieLocalDataSource
import uz.ttpu.movieshelf.data.remote.FakeMovieRemoteDataSource
import uz.ttpu.movieshelf.data.repository.MovieRepositoryImpl
import uz.ttpu.movieshelf.domain.repository.MovieRepository
import uz.ttpu.movieshelf.domain.usecase.GetMoviesUseCase
import uz.ttpu.movieshelf.domain.usecase.ToggleFavoriteUseCase

class AppContainer {
    val fakeRemote = FakeMovieRemoteDataSource()
    private val local: MovieLocalDataSource = InMemoryMovieLocalDataSource()
    private val repository: MovieRepository = MovieRepositoryImpl(fakeRemote, local)

    val getMovies = GetMoviesUseCase(repository)
    val toggleFavorite = ToggleFavoriteUseCase(repository)
}