package com.bookshelf.app.core.baseui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null

    abstract fun setOnclickListener()

    override fun onResume() {
        super.onResume()
        setOnclickListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
    }

    protected fun showProgress(show: Boolean) {
        progressDialog?.let {
            try {
                if (show && !it.isShowing) {
                    it.show()
                } else {
                    it.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
