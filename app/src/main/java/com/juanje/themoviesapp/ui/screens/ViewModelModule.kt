package com.juanje.themoviesapp.ui.screens

import android.content.Context
import com.juanje.data.repositories.MovieRepository
import com.juanje.data.repositories.UserRepository
import com.juanje.themoviesapp.common.ConnectivityObserver
import com.juanje.themoviesapp.common.NetworkConnectivityObserver
import com.juanje.usecases.LoadMovie
import com.juanje.usecases.LoadUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun loadUserProvider(userRepository: UserRepository) =
        LoadUser(userRepository)

    @Provides
    fun loadMovieProvider(movieRepository: MovieRepository) =
        LoadMovie(movieRepository)

    @Provides
    fun connectivityObserverProvider(@ApplicationContext applicationContext: Context): ConnectivityObserver =
        NetworkConnectivityObserver(applicationContext)
}