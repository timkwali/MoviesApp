package com.example.movieslist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.common.data.db.model.Movie
import com.example.common.utils.Constants.IMAGE_URL
import com.example.common.utils.Utils.loadImage
import com.example.movieslist.databinding.MoviesListItemBinding

class MoviesListAdapter(
    private val onClick: (Movie) -> Unit
): ListAdapter<Movie, MoviesListAdapter.MovieListViewHolder>(MovieDiffCallback) {

    inner class MovieListViewHolder(
        private val binding: MoviesListItemBinding, val onClick: (Movie) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                titleTv.text = movie.title
                releaseDate.text = movie.releaseDate

                val imageUrl = IMAGE_URL + movie.posterPath
                imageIv.loadImage(imageUrl)

                binding.root.setOnClickListener {
                    onClick(movie)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val binding = MoviesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieListViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

}

object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }
}