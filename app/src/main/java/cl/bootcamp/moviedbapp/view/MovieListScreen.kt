package cl.bootcamp.moviedbapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cl.bootcamp.moviedbapp.R
import cl.bootcamp.moviedbapp.components.AddMovieDialog
import cl.bootcamp.moviedbapp.components.AppBarView
import cl.bootcamp.moviedbapp.model.Movie
import cl.bootcamp.moviedbapp.viewModel.MovieViewModel
import coil.compose.AsyncImage

@Composable
fun MovieListScreen(viewModel: MovieViewModel) {
    val movieList by viewModel.movies.collectAsState(initial = emptyList())
    var showAddMovieDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { AppBarView(title = stringResource(R.string.app_name)) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddMovieDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Película")
            }
        }
    ) { padding ->
        if (movieList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No hay películas")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movieList) { movie ->
                    MovieCard(movie, onDelete = { viewModel.deleteMovie(movie) }, padding)
                }
            }
        }

        if (showAddMovieDialog) {
            AddMovieDialog(
                onDismiss = { showAddMovieDialog = false },
                onAddMovie = { newMovie ->
                    viewModel.addMovie(newMovie)
                }
            )
        }
    }
}


@Composable
fun MovieCard(movies: Movie, onDelete: () -> Unit, contentPadding: PaddingValues) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val topBarHeight = 30.dp
    val availableHeight = screenHeight - topBarHeight - contentPadding.calculateTopPadding()

    val calculatedHeight = availableHeight / 2

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(calculatedHeight)
            .padding(8.dp)
    ) {
        Box {
            Row(modifier = Modifier.padding(8.dp)) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movies.poster_path}",
                    contentDescription = movies.title,
                    modifier = Modifier
                        .height(calculatedHeight - 50.dp) // Ajustar para incluir márgenes internos
                        .width(calculatedHeight / 3) // Relación de aspecto 1:3
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(text = "Título: ${movies.title}", maxLines = 1)
                    Text(text = "ID: ${movies.id}")
                    Text(text = "Calificación: ${movies.vote_average}")
                    Text(text = "Fecha: ${movies.release_date}")
                    Text(text = "Descripción: ${movies.overview}")
                }
            }

            IconButton(
                onClick = { onDelete() },
                modifier = Modifier
                    .align(Alignment.TopEnd) // Posición en la esquina superior derecha
                    .padding(8.dp) // Espaciado respecto al borde
            ) {
                Icon(
                    imageVector = Icons.Default.Delete, // Ícono de borrar
                    contentDescription = "Delete Movie",
                    tint = MaterialTheme.colorScheme.error // Color de ícono
                )
            }
        }
    }
}
