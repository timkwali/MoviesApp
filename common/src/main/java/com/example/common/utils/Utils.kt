package com.example.common.utils

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.common.R
import com.example.common.data.api.model.response.genres.Genre
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar

object Utils {
    fun ImageView.loadImage(url: String, placeHolder: Int = R.drawable.tmd_logo) {
        Glide.with(this.context)
            .load(url)
            .placeholder(placeHolder)
            .error(placeHolder)
            .into(this)
    }

    fun Fragment.showSnackBar(
        message: String?,
        duration: Int = 3000,
        view: View? = requireView()
    ) {
        Snackbar.make(view!!, message!!, duration).show()
    }

    fun getGenreNameFromId(genreIds: List<Int>, genres: List<Genre>): List<String> {
        val movieGenres = mutableListOf<String>()
        genres.forEach { genre ->
            if(genreIds.contains(genre.id)) {
                movieGenres.add(genre.name.toString())
            }
        }
        return movieGenres
    }

    fun ChipGroup.addChips(tags: List<String>, setLimit: Boolean = false) {
        this.removeAllViews()
        var numOfChips = 0
        val limit = if(setLimit) 3 else Int.MAX_VALUE
        tags.forEach { tag ->
            if(tag.isNotEmpty() && numOfChips < limit) {
                val newChip = Chip(this.context)
                newChip.apply {
                    chipStrokeColor = ColorStateList.valueOf(resources.getColor(R.color.white))
                    chipBackgroundColor = ColorStateList.valueOf(resources.getColor(R.color.off_white))
                    text = tag
                    setTextAppearance(R.style.chipText)
                }
                this.addView(newChip)
                numOfChips++
            }
        }
    }
}