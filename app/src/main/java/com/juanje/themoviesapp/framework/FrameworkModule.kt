package com.juanje.themoviesapp.framework

import android.content.Context
import androidx.room.Room
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.data.datasources.LocalDataSource
import com.juanje.themoviesapp.data.datasources.RemoteDataSource
import com.juanje.themoviesapp.framework.data.database.MovieDatabaseDataSource
import com.juanje.themoviesapp.framework.data.database.MovieDatabase
import com.juanje.themoviesapp.framework.data.database.daos.MovieDao
import com.juanje.themoviesapp.framework.data.server.MovieService
import com.juanje.themoviesapp.framework.data.server.MovieServerDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun apiKeyProvider(@ApplicationContext applicationContext: Context): String =
        applicationContext.getString(R.string.api_key)

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider(@ApplicationContext applicationContext: Context): String =
        applicationContext.getString(R.string.base_url)

    @Provides
    @Singleton
    @Named("okHttpClient")
    fun okHttpClientProvider(): OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    @Provides
    @Singleton
    fun retrofitProvider(
        @Named("baseUrl") baseUrl: String,
        @Named("okHttpClient") okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun movieServiceProvider(retrofit: Retrofit): MovieService =
        retrofit.run { create(MovieService::class.java) }

    @Provides
    fun movieServerDataSourceProvider(movieService: MovieService): RemoteDataSource =
        MovieServerDataSource(movieService)

    @Provides
    @Singleton
    fun movieDatabaseProvider(@ApplicationContext applicationContext: Context): MovieDatabase =
        Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            name = applicationContext.getString(R.string.name_database)
        ).build()

    @Provides
    @Singleton
    fun movieDaoProvider(movieDatabase: MovieDatabase): MovieDao =
        movieDatabase.movieDao()

    @Provides
    fun movieDatabaseDataSourceProvider(movieDao: MovieDao): LocalDataSource =
        MovieDatabaseDataSource(movieDao)

}