package com.karasm.flocnews.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.karasm.flocnews.R
import com.karasm.flocnews.viewmodels.LoginViewModel

class LoginFragment:Fragment(R.layout.login_screen),View.OnClickListener {

    lateinit var loginField: TextInputEditText
    lateinit var passField: TextInputEditText

    lateinit var loginLayout: TextInputLayout
    lateinit var passLayout: TextInputLayout

    lateinit var logButton: Button
    lateinit var regButton:TextView


    lateinit var mViewModel:LoginViewModel

    companion object{
        fun newInstance():LoginFragment{
            return LoginFragment()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()

        mViewModel=ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initListeners()
    }

    private fun initViews(view:View){
        loginField=view.findViewById(R.id.textInputLogin)
        passField=view.findViewById(R.id.textInputPass)

        loginLayout=view.findViewById(R.id.textInputLoginLayout)
        passLayout=view.findViewById(R.id.textInputPassLayout)

        logButton=view.findViewById(R.id.logButton)
        regButton=view.findViewById(R.id.notRegisteredYet)
    }

    private fun initListeners(){
        logButton.setOnClickListener(this)
        regButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            logButton.id->{
                mViewModel.loginUser(loginField.text.toString(),passField.text.toString())
            }
            regButton.id->{
                fragmentManager!!
                    .beginTransaction()
                    .replace(R.id.fragment_container,RegisterFragment.newInstance())
                    .commit()
            }
        }
    }
}