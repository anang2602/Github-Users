package com.anangsw.githubuser.utils.extentions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, observer: (T) -> Unit) {
    liveData.observe(
        this
    ) {
        it?.let { t -> observer(t) }
    }
}


fun <T> LifecycleOwner.observe(liveData: MutableLiveData<T>, observer: (T) -> Unit) {
    liveData.observe(
        this
    ) {
        it?.let { t -> observer(t) }
    }
}

fun <T> LifecycleOwner.removeObservers(liveData: MutableLiveData<T>) {
    liveData.removeObservers(this)
}

fun <T> LifecycleOwner.removeObservers(liveData: LiveData<T>) {
    liveData.removeObservers(this)
}