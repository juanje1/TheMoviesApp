package com.juanje.themoviesapp.data

import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.data.datasources.UserLocalDataSource
import com.juanje.data.repositories.MovieRepository
import com.juanje.data.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun userRepositoryProvider(userLocalDataSource: UserLocalDataSource) =
        UserRepository(userLocalDataSource)

    @Provides
    fun movieRepositoryProvider(
        movieLocalDataSource: MovieLocalDataSource,
        movieRemoteDataSource: MovieRemoteDataSource,
        @Named("apiKey") apiKey: String
    ) = MovieRepository(movieLocalDataSource, movieRemoteDataSource, apiKey)
}