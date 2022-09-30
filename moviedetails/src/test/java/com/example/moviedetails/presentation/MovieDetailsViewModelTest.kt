package com.example.moviedetails.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.common.utils.Resource
import com.example.moviedetails.domain.usecase.GetGenre
import com.example.moviedetails.domain.usecase.GetMovie
import com.example.moviedetails.utils.Constants.genreList
import com.example.moviedetails.utils.Constants.popularMovies
import com.example.moviedetails.utils.Constants.toMovieList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieDetailsViewModelTest {
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var getMovie: GetMovie
    private lateinit var getGenre: GetGenre

    private suspend fun initialise() {

        getMovie = Mockito.mock(GetMovie::class.java)
        Mockito.`when`(getMovie(1)).thenReturn(flowOf(
            Resource.Success(popularMovies.toMovieList()[0])
        ))

        getGenre = Mockito.mock(GetGenre::class.java)
        Mockito.`when`(getGenre()).thenReturn(flowOf(
            Resource.Success(genreList)
        ))

        movieDetailsViewModel = MovieDetailsViewModel(getMovie, getGenre)
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `get movie by id returns correct movie`() = runTest {
        initialise()
        movieDetailsViewModel.getMovieById(1).join()
        val movie = movieDetailsViewModel.movie.value?.data
        assert(movie?.id == 1)
    }

    @Test
    fun `get genres returns list of resource of genres`() = runTest {
        initialise()
        movieDetailsViewModel.getGenres().join()
        val genreList = movieDetailsViewModel.genres.value?.data
        genreList?.isNotEmpty()?.let { assert(it) }
    }

    @Test
    fun `get genre names returns correct list of genre names`() = runTest {
        initialise()
        movieDetailsViewModel.getGenreNames(listOf(1, 2, 3, 4), genreList)
        val genreNames = movieDetailsViewModel.genreNames.value?.data
        assert(genreNames?.get(0) == "Action")
    }
}