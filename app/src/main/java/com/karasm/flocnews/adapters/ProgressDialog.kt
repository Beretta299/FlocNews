package com.karasm.flocnews.adapters

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.karasm.flocnews.R

class ProgressDialog(context: Context):Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_dialog)
    }

}