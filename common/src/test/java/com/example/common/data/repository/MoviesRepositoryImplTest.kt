package com.example.common.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.common.data.api.MoviesApi
import com.example.common.data.db.MoviesDatabase
import com.example.common.data.db.dao.GenreDao
import com.example.common.data.db.dao.MoviesDao
import com.example.common.utils.Constants.genreList
import com.example.common.utils.Constants.genreResponse
import com.example.common.utils.Constants.popularMovies
import com.example.common.utils.Constants.popularMoviesResponse
import com.example.common.utils.Constants.toMovieList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
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
class MoviesRepositoryImplTest {
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var moviesRepository: MoviesRepositoryImpl
    private lateinit var moviesApi: MoviesApi
    private lateinit var moviesDao: MoviesDao
    private lateinit var genreDao: GenreDao
    private lateinit var moviesDatabase: MoviesDatabase

    private suspend fun initialise() {
        moviesApi =  Mockito.mock(MoviesApi::class.java)
        Mockito.`when`(moviesApi.getPopularMovies()).thenReturn(popularMoviesResponse)
        Mockito.`when`(moviesApi.getMoviesGenre()).thenReturn(genreResponse)

        moviesDao = Mockito.mock(MoviesDao::class.java)
        Mockito.`when`(moviesDao.getAllMovies()).thenReturn(flowOf(popularMovies.toMovieList()))

        genreDao = Mockito.mock(GenreDao::class.java)
        Mockito.`when`(genreDao.getMoviesGenres()).thenReturn(flowOf(genreList))

        moviesDatabase = Mockito.mock(MoviesDatabase::class.java)
        Mockito.`when`(moviesDatabase.moviesDao()).thenReturn(moviesDao)
        Mockito.`when`(moviesDatabase.genreDao()).thenReturn(genreDao)

        moviesRepository = MoviesRepositoryImpl(moviesApi, moviesDatabase)
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
    fun `get api popular movies returns popular movies response`() = runTest {
        initialise()
        val moviesResponse = moviesRepository.getMovies()
        assert(moviesResponse.results.isNotEmpty())
    }

    @Test
    fun `get Db movies returns correct result`() = runTest {
        initialise()
        val dbMovies = moviesRepository.getDbMovies()
        assert(dbMovies.first().isNotEmpty())
    }

    @Test
    fun `get genres returns genre response`() = runTest {
        initialise()
        val genreResponse = moviesRepository.getGenres()
        genreResponse.genres?.isNotEmpty()?.let { assert(it) }
    }

    @Test
    fun `get dB genres returns correct result`() = runTest {
        initialise()
        val genreList = moviesRepository.getDbGenres().first()
        assert(genreList.isNotEmpty())
    }
}