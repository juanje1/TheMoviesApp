package com.juanje.themoviesapp.ui.screens.detail

import com.juanje.data.repositories.MovieRepository
import com.juanje.themoviesapp.ui.screens.CoroutinesTestRule
import com.juanje.themoviesapp.ui.screens.FakeMovieLocalDataSource
import com.juanje.themoviesapp.ui.screens.FakeMovieRemoteDataSource
import com.juanje.themoviesapp.ui.screens.fakeId
import com.juanje.themoviesapp.ui.screens.fakeMovies
import com.juanje.themoviesapp.ui.screens.fakeUserName
import com.juanje.usecases.LoadMovie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    private val apiKey = "d30e1f350220f9aad6c4110df385d380"
    private val mainDispatcher = Mockito.mock<CoroutineDispatcher>()
    private lateinit var detailViewModel: DetailViewModel

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
        val movieLocalDataSource = FakeMovieLocalDataSource()
        val movieRemoteDataSource = FakeMovieRemoteDataSource(fakeMovies)
        val movieRepository = MovieRepository(movieLocalDataSource, movieRemoteDataSource, apiKey)
        val loadMovie = LoadMovie(movieRepository)
        detailViewModel = DetailViewModel(loadMovie, mainDispatcher)
    }

    @Test
    fun `Listening to movie detail from the database`() = runTest {
        detailViewModel.getMovieDetail(fakeUserName, fakeId)
        val movie = detailViewModel.state.value.movie

        Assert.assertEquals(fakeUserName, movie?.userName)
        Assert.assertEquals(fakeId, movie?.id)
    }
}