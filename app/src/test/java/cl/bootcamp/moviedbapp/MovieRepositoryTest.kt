package cl.bootcamp.moviedbapp


import android.arch.core.executor.testing.InstantTaskExecutorRule
import cl.bootcamp.moviedbapp.dataSource.RestDataSource
import cl.bootcamp.moviedbapp.model.Movie
import cl.bootcamp.moviedbapp.repository.MovieRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Mock data
private val movie1 = Movie(1, "Pelicula 1", "Descripcion 1", "/path1.jpg", "2024-01-01", 8.0)
private val movie2 = Movie(2, "Pelicula 2", "Descripcion 2", "/path2.jpg", "2024-02-01", 7.5)
private val movie3 = Movie(3, "Pelicula 3", "Descripcion 3", "/path3.jpg", "2024-03-01", 9.0)


class MovieRepositoryTest {

    private val mockWebServer: MockWebServer = MockWebServer().apply {
        url("/")
        dispatcher = myDispatcher
    }

    private val restDataSource = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RestDataSource::class.java)

    private val movieRepository = MovieRepositoryImpl(restDataSource, MockMovieDao())

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Comprueba que la insercion de datos a la DB funciona correctamente`() = runBlocking {
        // Simulamos la obtención de películas desde la API
        val moviesFromApi = listOf(movie1, movie2, movie3)
        moviesFromApi.forEach { movieRepository.addMovieRoom(it) }

        // Obtenemos las películas almacenadas en la base de datos
        val moviesFromDb = movieRepository.getPopularMoviesRoom().first()

        // Verificamos que las películas insertadas coincidan
        assertEquals(3, moviesFromDb.size)
        assertEquals("Pelicula 1", moviesFromDb[0].title)
        assertEquals("Pelicula 2", moviesFromDb[1].title)
        assertEquals("Pelicula 3", moviesFromDb[2].title)
    }

    @Test
    fun `Comprueba si la eliminacion de una pelicula funciona correctamente`() = runBlocking {
        // Simulamos la inserción de películas
        val moviesFromApi = listOf(movie1, movie2, movie3)
        moviesFromApi.forEach { movieRepository.addMovieRoom(it) }

        // Eliminamos una película específica
        movieRepository.deleteMovieRoom(movie2)

        // Obtenemos las películas restantes de la base de datos
        val moviesFromDb = movieRepository.getPopularMoviesRoom().first()

        // Verificamos que la película eliminada no esté en la base de datos
        assertEquals(2, moviesFromDb.size)
        assertEquals("Pelicula 1", moviesFromDb[0].title)
        assertEquals("Pelicula 3", moviesFromDb[1].title)
    }
}

// Mock de MovieDao
class MockMovieDao : cl.bootcamp.moviedbapp.model.MovieDao {
    private val movies = MutableStateFlow<List<Movie>>(listOf())

    override suspend fun insertMovieRoom(movie: Movie) {
        movies.value = movies.value.toMutableList().apply { add(movie) }
    }

    override fun getALlMoviesRoom() = movies

    override suspend fun deleteMovieRoom(movie: Movie) {
        movies.value = movies.value.toMutableList().apply { remove(movie) }
    }
}
// Dispatcher para MockWebServer
val myDispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/movies/popular" -> MockResponse().apply { addResponse("api_movies.json") }
            else -> MockResponse().setResponseCode(404)
        }
    }
}

// Función para agregar respuesta desde un archivo mock
fun MockResponse.addResponse(filePath: String): MockResponse {
    val inputStream = javaClass.classLoader?.getResourceAsStream(filePath)
    val source = inputStream?.source()?.buffer()
    source?.let {
        setResponseCode(200)
        setBody(it.readString(Charsets.UTF_8))
    }
    return this
}