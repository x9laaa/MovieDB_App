package cl.bootcamp.moviedbapp.dataSource

import cl.bootcamp.moviedbapp.model.MovieResponse
import cl.bootcamp.moviedbapp.util.Constants.Companion.API_KEY
import cl.bootcamp.moviedbapp.util.Constants.Companion.ENDPOINT_MOVIES_POPULAR
import retrofit2.http.GET
import retrofit2.http.Query

interface RestDataSource {
    @GET(ENDPOINT_MOVIES_POPULAR)
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY
    ): MovieResponse
}