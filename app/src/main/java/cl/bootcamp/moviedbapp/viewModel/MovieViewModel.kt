package cl.bootcamp.moviedbapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.bootcamp.moviedbapp.model.Movie
import cl.bootcamp.moviedbapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    val movies: Flow<List<Movie>> by lazy {
        repository.getPopularMoviesRoom()
    }

    fun loadMoviesFromApi() {
        viewModelScope.launch {
            repository.getPopularMoviesApi()
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            repository.deleteMovieRoom(movie)
        }
    }

    fun addMovie(movie: Movie) {
        viewModelScope.launch {
            repository.addMovieRoom(movie)
        }


    }
}