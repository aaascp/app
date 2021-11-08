package com.aaascp.commons

import android.content.Context
import android.widget.ImageView
import coil.Coil
import coil.ImageLoader
import coil.loadAny
import coil.request.ImageRequest

fun initImageLoader(context: Context) {
    val imageLoader = ImageLoader.Builder(context)
        .crossfade(true)
        .build()

    Coil.setImageLoader(imageLoader)
}

fun ImageView.load(data: Any?) {
    loadAny(data) {
        defaultBuilder(this@load)
    }
}

private fun ImageRequest.Builder.defaultBuilder(imageView: ImageView) =
    this.target(imageView)
        .fallback(R.drawable.ic_broken_image)
        .error(R.drawable.ic_broken_image)
