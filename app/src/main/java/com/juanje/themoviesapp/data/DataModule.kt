package com.juanje.themoviesapp.data

import com.juanje.data.datasources.LocalDataSource
import com.juanje.data.datasources.RemoteDataSource
import com.juanje.data.repositories.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun movieRepositoryProvider(
        movieDatabaseDataSource: LocalDataSource,
        movieServerDataSource: RemoteDataSource,
        @Named("apiKey") apiKey: String
    ) = MovieRepository(movieDatabaseDataSource, movieServerDataSource, apiKey)

}