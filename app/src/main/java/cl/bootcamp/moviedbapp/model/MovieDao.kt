package cl.bootcamp.moviedbapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieRoom(movie: Movie)

    @Query("SELECT * FROM movie")
    fun getALlMoviesRoom(): Flow<List<Movie>>
}