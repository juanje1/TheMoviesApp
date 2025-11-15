package com.juanje.themoviesapp

import android.content.Context
import androidx.room.Room
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.data.datasources.UserLocalDataSource
import com.juanje.themoviesapp.data.FrameworkModule
import com.juanje.themoviesapp.data.IoDispatcher
import com.juanje.themoviesapp.data.MainDispatcher
import com.juanje.themoviesapp.data.database.TheMoviesAppDatabase
import com.juanje.themoviesapp.data.database.daos.MovieDao
import com.juanje.themoviesapp.data.database.daos.UserDao
import com.juanje.themoviesapp.data.database.datasources.MovieDatabaseDataSource
import com.juanje.themoviesapp.data.database.datasources.UserDatabaseDataSource
import com.juanje.themoviesapp.data.server.datasources.MovieServerDataSource
import com.juanje.themoviesapp.data.server.services.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FrameworkModule::class]
)
object TestFrameworkModule {

    @Provides
    @Singleton
    @Named("apiKey")
    fun apiKeyProvider(@ApplicationContext applicationContext: Context): String =
        applicationContext.getString(R.string.api_key)

    @Provides
    @Singleton
    @MainDispatcher
    fun mainDispatcherProvider(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    @IoDispatcher
    fun ioDispatcherProvider(): CoroutineDispatcher = Dispatchers.IO

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
    fun provideInMemoryDatabase(@ApplicationContext context: Context): TheMoviesAppDatabase {
        return Room.inMemoryDatabaseBuilder(context, TheMoviesAppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun userDaoProvider(theMoviesAppDatabase: TheMoviesAppDatabase): UserDao =
        theMoviesAppDatabase.userDao()

    @Provides
    @Singleton
    fun movieDaoProvider(theMoviesAppDatabase: TheMoviesAppDatabase): MovieDao =
        theMoviesAppDatabase.movieDao()

    @Provides
    @Singleton
    fun movieServiceProvider(retrofit: Retrofit): MovieService =
        retrofit.run { create(MovieService::class.java) }

    @Provides
    fun userDatabaseDataSourceProvider(userDao: UserDao, @IoDispatcher testIoDispatcher: CoroutineDispatcher): UserLocalDataSource =
        UserDatabaseDataSource(userDao, testIoDispatcher)

    @Provides
    fun movieDatabaseDataSourceProvider(movieDao: MovieDao): MovieLocalDataSource =
        MovieDatabaseDataSource(movieDao)

    @Provides
    fun movieServerDataSourceProvider(movieService: MovieService): MovieRemoteDataSource =
        MovieServerDataSource(movieService)
}
