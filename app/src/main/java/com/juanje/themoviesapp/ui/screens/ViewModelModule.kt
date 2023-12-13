package com.juanje.themoviesapp.ui.screens

import com.juanje.data.repositories.MovieRepository
import com.juanje.data.repositories.UserRepository
import com.juanje.usecases.LoadMovie
import com.juanje.usecases.LoadUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun loadUserProvider(userRepository: UserRepository) = LoadUser(userRepository)

    @Provides
    fun loadMovieProvider(movieRepository: MovieRepository) = LoadMovie(movieRepository)

}