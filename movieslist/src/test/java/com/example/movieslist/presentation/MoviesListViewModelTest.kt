package com.example.movieslist.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.common.utils.Resource
import com.example.movieslist.domain.usecase.GetGenres
import com.example.movieslist.domain.usecase.GetMovies
import com.example.movieslist.utils.Constants.genreList
import com.example.movieslist.utils.Constants.popularMovies
import com.example.movieslist.utils.Constants.toMovieList
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
class MoviesListViewModelTest {

    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var moviesListViewModel: MoviesListViewModel
    private lateinit var getMovies: GetMovies
    private lateinit var getGenres: GetGenres

    private suspend fun initialise() {

        getMovies = Mockito.mock(GetMovies::class.java)
        Mockito.`when`(getMovies()).thenReturn(flowOf(
            Resource.Success(popularMovies.toMovieList())
        ))

        getGenres = Mockito.mock(GetGenres::class.java)
        Mockito.`when`(getGenres()).thenReturn(flowOf(
            Resource.Success(genreList)
        ))

        moviesListViewModel = MoviesListViewModel(getMovies, getGenres)
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
    fun `get movies returns list of resource of list of movies`() = runTest {
        initialise()
        moviesListViewModel.getMoviesList().join()
        val moviesList = moviesListViewModel.moviesListResponse.value?.data
        moviesList?.isNotEmpty()?.let { assert(it) }
    }

    @Test
    fun `get genres returns list of resource of genres`() = runTest {
        initialise()
        moviesListViewModel.getGenreList().join()
        val genreList = moviesListViewModel.genreList.value?.data
        genreList?.isNotEmpty()?.let { assert(it) }
    }
}