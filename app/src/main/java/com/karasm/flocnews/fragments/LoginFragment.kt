package com.karasm.flocnews.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.karasm.flocnews.R

class LoginFragment:Fragment(R.layout.login_screen) {

    companion object{
        fun newInstance():LoginFragment{
            return LoginFragment()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}