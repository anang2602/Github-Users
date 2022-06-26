package com.anangsw.githubuser.utils

import kotlinx.coroutines.Dispatchers

object DispatcherHelper {
    val IO = Dispatchers.IO
    val MAIN = Dispatchers.Main
    val DEFAULT = Dispatchers.Default
    val UNCONFINED = Dispatchers.Unconfined
}