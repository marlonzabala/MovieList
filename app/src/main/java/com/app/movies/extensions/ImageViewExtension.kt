package com.app.movies.extensions

import android.widget.ImageView
import com.app.movies.R
import com.app.movies.network.model.ItunesItem
import com.app.movies.utils.Converter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.load(imageAddress: String) {
    val requestOption = RequestOptions()
        .centerCrop()
        .dontTransform()
        .dontAnimate()

    Glide.with(this)
        .load(imageAddress)
        .apply(requestOption)
        .into(this)
}