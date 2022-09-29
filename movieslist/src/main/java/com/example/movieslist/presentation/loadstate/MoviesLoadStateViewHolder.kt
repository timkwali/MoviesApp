package com.example.movieslist.presentation.loadstate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.movieslist.databinding.MoviesLoadStateFooterViewItemBinding

class MoviesLoadStateViewHolder (
    private val binding: MoviesLoadStateFooterViewItemBinding,
    retry: () -> Unit
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if(loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }

        binding.apply {
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error
            errorMsg.isVisible = loadState is LoadState.Error
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): MoviesLoadStateViewHolder {
            val binding = MoviesLoadStateFooterViewItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return MoviesLoadStateViewHolder(binding, retry)
        }
    }
}