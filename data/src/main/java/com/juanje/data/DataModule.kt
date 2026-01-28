@file:Suppress("unused")
package com.juanje.data

import com.juanje.data.repositories.MovieRepositoryImpl
import com.juanje.data.repositories.UserRepositoryImpl
import com.juanje.domain.repositories.MovieRepository
import com.juanje.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository
}