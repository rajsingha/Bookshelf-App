package com.bookshelf.app.core.baseui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bookshelf.app.R


class ProgressDialog constructor(context: Context) : AlertDialog(context) {

    init {
        try {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
            val view: View =
                LayoutInflater.from(getContext()).inflate(R.layout.layout_progress_dialog, null)
            setView(view)
        } catch (e: Exception) {
            Log.e(ProgressDialog::class.java.simpleName, e.toString())
        }
    }
}