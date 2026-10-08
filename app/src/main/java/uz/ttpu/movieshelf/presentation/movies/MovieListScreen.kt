package uz.ttpu.movieshelf.presentation.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.ttpu.movieshelf.domain.model.Movie

@Composable
fun MovieListScreen(
    state: MovieListUiState,
    onFavoriteClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (state) {
            is MovieListUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MovieListUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRefresh) {
                        Text("Retry")
                    }
                }
            }

            is MovieListUiState.Success -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onRefresh) {
                            Text("Refresh")
                        }
                    }

                    if (state.isFromCache) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Offline — showing saved data",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
                    ) {
                        items(items = state.movies, key = { it.id }) { movie ->
                            MovieItem(
                                movie = movie,
                                onFavoriteClick = { onFavoriteClick(movie.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${movie.year} · Rating ${movie.rating}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (movie.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (movie.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ============================================================================
// Previews
// ============================================================================

private val sampleMovies = listOf(
    Movie(id = 1, title = "Echoes of Khiva", year = 2022, rating = 9.0, isFavorite = true),
    Movie(id = 2, title = "The Silk Road Express", year = 2019, rating = 8.4, isFavorite = false),
    Movie(id = 3, title = "Midnight in Samarkand", year = 2021, rating = 7.9, isFavorite = false)
)

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun MovieListScreenLoadingPreview() {
    MaterialTheme {
        MovieListScreen(
            state = MovieListUiState.Loading,
            onFavoriteClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, name = "Success Online State")
@Composable
private fun MovieListScreenSuccessPreview() {
    MaterialTheme {
        MovieListScreen(
            state = MovieListUiState.Success(
                movies = sampleMovies,
                isFromCache = false
            ),
            onFavoriteClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, name = "Success Cache State")
@Composable
private fun MovieListScreenSuccessCachePreview() {
    MaterialTheme {
        MovieListScreen(
            state = MovieListUiState.Success(
                movies = sampleMovies,
                isFromCache = true
            ),
            onFavoriteClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun MovieListScreenErrorPreview() {
    MaterialTheme {
        MovieListScreen(
            state = MovieListUiState.Error("Can't load movies. Check your connection and try again."),
            onFavoriteClick = {},
            onRefresh = {}
        )
    }
}