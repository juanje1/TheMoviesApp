package com.juanje.themoviesapp

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.data.datasources.UserLocalDataSource
import com.juanje.themoviesapp.data.database.TheMoviesAppDatabase
import com.juanje.themoviesapp.data.database.daos.MovieDao
import com.juanje.themoviesapp.data.database.daos.UserDao
import com.juanje.themoviesapp.data.database.datasources.MovieDatabaseDataSource
import com.juanje.themoviesapp.data.database.datasources.UserDatabaseDataSource
import com.juanje.themoviesapp.data.server.MovieServerDataSource
import com.juanje.themoviesapp.data.server.MovieService
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
    fun movieServerDataSourceProvider(movieService: MovieService): MovieRemoteDataSource =
        MovieServerDataSource(movieService)

    @Provides
    @Singleton
    fun getMigration1To2(): Migration =
        object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE MovieEntity ADD COLUMN userName TEXT NOT NULL DEFAULT ''"
                )
            }
        }

    @Provides
    @Singleton
    fun theMoviesAppDatabaseProvider(@ApplicationContext applicationContext: Context)
    : TheMoviesAppDatabase = Room.databaseBuilder(
        applicationContext,
        TheMoviesAppDatabase::class.java,
        name = applicationContext.getString(R.string.name_database)
    ).addMigrations(getMigration1To2()).build()

    @Provides
    @Singleton
    fun movieDaoProvider(theMoviesAppDatabase: TheMoviesAppDatabase): MovieDao =
        theMoviesAppDatabase.movieDao()

    @Provides
    @Singleton
    fun userDaoProvider(theMoviesAppDatabase: TheMoviesAppDatabase): UserDao =
        theMoviesAppDatabase.userDao()

    @Provides
    fun movieDatabaseDataSourceProvider(movieDao: MovieDao)
    : MovieLocalDataSource = MovieDatabaseDataSource(movieDao)

    @Provides
    fun userDatabaseDataSourceProvider(userDao: UserDao)
    : UserLocalDataSource = UserDatabaseDataSource(userDao)

}