package com.juanje.themoviesapp.hilt

import android.content.Context
import androidx.room.Room
import com.juanje.data.datasources.FavoriteLocalDataSource
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.data.datasources.UserLocalDataSource
import com.juanje.framework.FrameworkModule
import com.juanje.framework.database.TheMoviesAppDatabase
import com.juanje.framework.database.daos.FavoriteDao
import com.juanje.framework.database.daos.MovieDao
import com.juanje.framework.database.daos.UserDao
import com.juanje.framework.database.datasources.FavoriteDatabaseDataSource
import com.juanje.framework.database.datasources.MovieDatabaseDataSource
import com.juanje.framework.database.datasources.UserDatabaseDataSource
import com.juanje.framework.server.datasources.MovieServerDataSource
import com.juanje.framework.server.services.MovieService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
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
abstract class TestFrameworkModule {
    companion object {
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
        fun favoriteDaoProvider(theMoviesAppDatabase: TheMoviesAppDatabase): FavoriteDao =
            theMoviesAppDatabase.favoriteDao()

        @Provides
        @Singleton
        fun movieServiceProvider(retrofit: Retrofit): MovieService =
            retrofit.run { create(MovieService::class.java) }
    }

    @Binds
    abstract fun bindUserLocalDataSource(userDatabaseDataSource: UserDatabaseDataSource): UserLocalDataSource

    @Binds
    abstract fun bindMovieLocalDataSource(movieDatabaseDataSource: MovieDatabaseDataSource): MovieLocalDataSource

    @Binds
    abstract fun bindMovieServerDataSource(movieServerDataSource: MovieServerDataSource): MovieRemoteDataSource

    @Binds
    abstract fun bindFavoriteLocalDataSource(favoriteDatabaseDataSource: FavoriteDatabaseDataSource): FavoriteLocalDataSource
}