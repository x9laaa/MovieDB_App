package cl.bootcamp.moviedbapp.model

data class MovieResponse(
    val page: Int,
    val results: List<Movies>
)

data class Movies(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double
)
