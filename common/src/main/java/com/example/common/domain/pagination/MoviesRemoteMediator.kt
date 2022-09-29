package com.example.common.domain.pagination

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.common.data.api.MoviesApi
import com.example.common.data.db.MoviesDatabase
import com.example.common.data.db.RemoteKeys
import com.example.common.data.db.model.Movie
import com.example.common.data.db.model.MovieItemMapper
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesDatabase: MoviesDatabase
): RemoteMediator<Int, Movie>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val page = when(loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
//            val apiQuery = query + Constants.LOGIN_IN_QUALIFIER
//            val apiResponse = service.getUsers(apiQuery, page, state.config.pageSize)

            val apiResponse = moviesApi.getPopularMovies(page = page)

            val movies = apiResponse.results
                .map { MovieItemMapper().mapToDomain(it) }
                ?: return MediatorResult.Error(Exception())
            val endOfPaginationReached = movies.isEmpty()

            moviesDatabase.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    moviesDatabase.remoteKeysDao().clearRemoteKeys()
                    moviesDatabase.moviesDao().clearMovies()
                }
                val prevKey = if(page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if(endOfPaginationReached) null else page + 1
                val keys = movies.map {
                    RemoteKeys(prevKey = prevKey, nextKey = nextKey)
                }

                moviesDatabase.remoteKeysDao().insertAll(keys)
                moviesDatabase.moviesDao().insertAll(movies)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: IOException) {
            delay(500)
            Log.d("exception", exception.toString())
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            delay(500)
            Log.d("exception", exception.toString())
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                moviesDatabase.remoteKeysDao().remoteKeysMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                moviesDatabase.remoteKeysDao().remoteKeysMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                moviesDatabase.remoteKeysDao().remoteKeysMovieId(movieId)
            }
        }
    }
}