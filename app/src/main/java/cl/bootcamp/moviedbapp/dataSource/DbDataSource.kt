package cl.bootcamp.moviedbapp.dataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import cl.bootcamp.moviedbapp.model.Movie
import cl.bootcamp.moviedbapp.model.MovieDao

@Database(entities = [Movie::class], version = 1)
abstract class DbDataSource: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}