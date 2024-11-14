package cl.bootcamp.moviedbapp.repository

import cl.bootcamp.moviedbapp.dataSource.RestDataSource
import cl.bootcamp.moviedbapp.model.MovieResponse
import javax.inject.Inject

interface MovieRepository {
    suspend fun getPopularMovies(): MovieResponse
}

class MovieRepositoryImpl @Inject constructor(
    private val restDataSource: RestDataSource
) : MovieRepository {

    override suspend fun getPopularMovies(): MovieResponse {
        return restDataSource.getPopularMovies()
    }
}