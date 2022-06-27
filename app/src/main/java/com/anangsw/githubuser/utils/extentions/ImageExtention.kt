package com.anangsw.githubuser.utils.extentions

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.anangsw.githubuser.R
import com.bumptech.glide.Glide

fun ImageView.loadAvatar(context: Context, url: String) {
    Glide.with(context)
        .load(url)
        .error(ContextCompat.getDrawable(context, R.drawable.avatar_placeholder))
        .into(this)
}