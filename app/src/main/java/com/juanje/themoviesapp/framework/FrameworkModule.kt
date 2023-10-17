package com.juanje.themoviesapp.framework

import android.content.Context
import androidx.room.Room
import com.juanje.themoviesapp.data.datasources.LocalDataSource
import com.juanje.themoviesapp.data.datasources.RemoteDataSource
import com.juanje.themoviesapp.framework.data.database.DatabaseDataSource
import com.juanje.themoviesapp.framework.data.database.MovieDatabase
import com.juanje.themoviesapp.framework.data.database.daos.MovieDao
import com.juanje.themoviesapp.framework.data.server.MovieService
import com.juanje.themoviesapp.framework.data.server.ServerDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FrameworkModule {

    @Provides
    @Singleton
    @Named("apiKey")
    fun apiKeyProvider(): String = "d30e1f350220f9aad6c4110df385d380"

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider(): String = "https://api.themoviedb.org/3/"

    @Provides
    @Singleton
    fun retrofitProvider(@Named("baseUrl") baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun movieServiceProvider(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    fun serverDataSourceProvider(movieService: MovieService): RemoteDataSource =
        ServerDataSource(movieService)

    @Provides
    @Singleton
    fun movieDatabaseProvider(@ApplicationContext applicationContext: Context): MovieDatabase =
        Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            name = "movies-db"
        ).build()

    @Provides
    @Singleton
    fun movieDaoProvider(movieDatabase: MovieDatabase): MovieDao =
        movieDatabase.movieDao()

    @Provides
    fun databaseDataSourceProvider(movieDao: MovieDao): LocalDataSource =
        DatabaseDataSource(movieDao)

}