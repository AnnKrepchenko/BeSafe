package com.krepchenko.besafe.ui.activities

import android.support.v7.app.AppCompatActivity
import com.krepchenko.besafe.ui.dialogs.LoadingDialog

abstract class BaseActivity : AppCompatActivity() {

    protected var TAG = this.javaClass.canonicalName
    private val DIALOG_TAG = "dialog_tag"

    private var loadingDialog: LoadingDialog? = null


    fun startLoadingDialog() {
        stopLoadingDialog()
        runOnUiThread {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog.start(supportFragmentManager, DIALOG_TAG)
            }
        }
    }

    fun stopLoadingDialog() {
        runOnUiThread {
            if (loadingDialog != null && !isFinishing) {
                loadingDialog?.dismissAllowingStateLoss()
                loadingDialog = null
            }
        }
    }
}