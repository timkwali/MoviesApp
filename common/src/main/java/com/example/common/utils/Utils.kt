package com.example.common.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.common.R

object Utils {
    fun ImageView.loadImage(url: String, placeHolder: Int = R.drawable.tmd_logo) {
        Glide.with(this.context)
            .load(url)
            .placeholder(placeHolder)
            .error(placeHolder)
            .into(this)
    }
}