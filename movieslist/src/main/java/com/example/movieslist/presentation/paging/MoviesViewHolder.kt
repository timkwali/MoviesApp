package com.example.movieslist.presentation.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.common.data.db.model.Movie
import com.example.common.utils.Constants.IMAGE_URL
import com.example.common.utils.Utils.loadImage
import com.example.movieslist.databinding.MoviesListItemBinding

class MoviesViewHolder(private val binding: MoviesListItemBinding):
    RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: Movie) {
        binding.apply {
            titleTv.text = movie.title
            releaseDate.text = movie.releaseDate
            val imageUrl = IMAGE_URL + movie.posterPath
            imageIv.loadImage(imageUrl)
        }
    }

    companion object {
        fun create(parent: ViewGroup): MoviesViewHolder {
            val binding = MoviesListItemBinding.inflate(
                LayoutInflater
                .from(parent.context), parent, false)
            return MoviesViewHolder(binding)
        }
    }
}