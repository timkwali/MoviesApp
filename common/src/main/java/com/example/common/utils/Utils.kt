package com.example.common.utils

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.common.R
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
}