package com.example.movieslist.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.example.common.data.db.model.Movie
import com.example.common.utils.Utils.showSnackBar
import com.example.movieslist.databinding.FragmentMoviesListBinding
import com.example.movieslist.presentation.loadstate.MoviesLoadStateAdapter
import com.example.movieslist.presentation.paging.MoviesListAdapter
import com.example.movieslist.presentation.paging.RemotePresentationState
import com.example.movieslist.presentation.paging.asRemotePresentationState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@AndroidEntryPoint
class MoviesListFragment : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding: FragmentMoviesListBinding get() = _binding!!
    private val moviesViewModel: MoviesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bindState(
            uiState = moviesViewModel.state,
            pagingData = moviesViewModel.pagingDataFlow,
            uiActions = moviesViewModel.accept
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun FragmentMoviesListBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<Movie>>,
        uiActions: (UiAction) -> Unit
    ) {
        val moviesListAdapter = MoviesListAdapter()
        val header = MoviesLoadStateAdapter { moviesListAdapter.retry() }
        moviesListRv.adapter = moviesListAdapter.withLoadStateFooter(
            footer = MoviesLoadStateAdapter { moviesListAdapter.retry() }
        )

        bindList(
            header = header,
            moviesListAdapter = moviesListAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun FragmentMoviesListBinding.bindList(
        header: MoviesLoadStateAdapter,
        moviesListAdapter: MoviesListAdapter,
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<Movie>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {

        retryBtn.setOnClickListener { moviesListAdapter.retry() }
        swipeSrl.setOnRefreshListener { moviesListAdapter.refresh() }
        swipeSrl.setColorSchemeResources(com.example.common.R.color.black)
        moviesListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })

        //used with remote mediator
        val notLoading = moviesListAdapter.loadStateFlow
            .asRemotePresentationState()
            .map { it == RemotePresentationState.PRESENTED }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(moviesListAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) moviesListRv.scrollToPosition(0)
            }
        }

        lifecycleScope.launch {
            moviesListAdapter.loadStateFlow.collect { loadState ->

                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && moviesListAdapter.itemCount > 0 }
                    ?: loadState.prepend

                val isListEmpty = loadState.refresh is LoadState.NotLoading && moviesListAdapter.itemCount == 0
                // show empty list
                emptyListTv.isVisible = isListEmpty
                // Only show the list if refresh succeeds, either from the the local db or the remote.
                moviesListRv.isVisible =  loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
//                progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                swipeSrl.isRefreshing = loadState.mediator?.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                retryBtn.isVisible = loadState.mediator?.refresh is LoadState.Error && moviesListAdapter.itemCount == 0
                // Show the error message if initial load or refresh fails.
                emptyListTv.isVisible = loadState.mediator?.refresh is LoadState.Error && moviesListAdapter.itemCount == 0
                if(loadState.mediator?.refresh is LoadState.Error && moviesListAdapter.itemCount == 0) {
                    val errSt = loadState.mediator?.refresh as LoadState.Error
                    emptyListTv.text = errSt.error.localizedMessage
                }
                if(isListEmpty) {
                    emptyListTv.text = "No movies found!"
                    retryBtn.isVisible = true
                    emptyListTv.isVisible = true
                }


                // Show error in snackbar
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    emptyListTv.text = it.error.localizedMessage
                    showSnackBar("\uD83D\uDE28 Wooops ${it.error.localizedMessage}")
                }
            }
        }
    }
}