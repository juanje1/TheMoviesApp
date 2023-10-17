package com.juanje.themoviesapp.framework.ui.screens.home

import com.juanje.themoviesapp.data.repositories.MovieRepository
import com.juanje.themoviesapp.usecases.LoadPopularMovies
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HomeActivityModule {

    @Provides
    fun loadPopularMoviesProvider(repository: MovieRepository) = LoadPopularMovies(repository)

}