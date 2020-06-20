package com.android.pixapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.android.pixapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Binding adapter used to display images from URL using Glide
 */
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    if(url != null) {
        val requestOptions = RequestOptions().placeholder(R.drawable.ic_placeholder)
        Glide.with(imageView.context).setDefaultRequestOptions(requestOptions).load(url).into(imageView)
    }
}