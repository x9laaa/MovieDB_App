package cl.bootcamp.moviedbapp.di

import cl.bootcamp.moviedbapp.repository.MovieRepository
import cl.bootcamp.moviedbapp.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun movieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository


}