package com.anangsw.githubuser.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.anangsw.githubuser.BuildConfig
import com.anangsw.githubuser.R
import com.anangsw.githubuser.utils.extentions.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.scottyab.rootbeer.RootBeer
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

open class BaseActivity<VM : ViewModel, VB : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory<VM>
    lateinit var viewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInitDataBinding()
    }

    open fun onInitDataBinding() {
        viewBinding = DataBindingUtil.setContentView(this, layoutRes)
        viewBinding.executePendingBindings()
        checkIsDeviceRooted()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.unbind()
    }

    open fun showErrorSnackBar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun checkIsDeviceRooted() {
        if (!BuildConfig.DEBUG) {
            val rootBeer = RootBeer(this)
            if (rootBeer.isRooted) {
                val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
                    .setMessage(R.string.text_alert_rooted)
                    .setPositiveButton(R.string.text_alert_button_tutup) { _, _ ->
                        finishAndRemoveTask()
                    }.setCancelable(false)
                dialog.show()
            }
        }
    }

}