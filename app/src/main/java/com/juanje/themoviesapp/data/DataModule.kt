package com.juanje.themoviesapp.data

import com.juanje.themoviesapp.data.datasources.LocalDataSource
import com.juanje.themoviesapp.data.datasources.RemoteDataSource
import com.juanje.themoviesapp.data.repositories.MovieRepository
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
        databaseDataSource: LocalDataSource,
        serverDataSource: RemoteDataSource,
        @Named("apiKey") apiKey: String
    ) = MovieRepository(databaseDataSource, serverDataSource, apiKey)

}