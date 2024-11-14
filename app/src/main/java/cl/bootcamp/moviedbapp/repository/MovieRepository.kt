package cl.bootcamp.moviedbapp.repository

import cl.bootcamp.moviedbapp.dataSource.RestDataSource
import cl.bootcamp.moviedbapp.model.Movie
import cl.bootcamp.moviedbapp.model.MovieDao
import cl.bootcamp.moviedbapp.model.MovieResponse
import cl.bootcamp.moviedbapp.model.Movies
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MovieRepository {
    suspend fun getPopularMoviesApi(): MovieResponse
    fun getPopularMoviesRoom(): Flow<List<Movie>>
}

class MovieRepositoryImpl @Inject constructor(
    private val restDataSource: RestDataSource,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getPopularMoviesApi(): MovieResponse {
        val data = restDataSource.getPopularMovies()
        data.results.forEach {
            val movie = Movie(it.id ,it.title, it.overview, it.poster_path, it.release_date, it.vote_average)
            movieDao.insertMovieRoom(movie)
        }
        return data
    }

    override fun getPopularMoviesRoom(): Flow<List<Movie>> {
        return movieDao.getALlMoviesRoom()
    }
}