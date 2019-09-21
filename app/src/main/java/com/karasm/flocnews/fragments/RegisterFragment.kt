package com.karasm.flocnews.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.karasm.flocnews.R
import com.karasm.flocnews.viewmodels.RegisterViewModel

class RegisterFragment: Fragment(R.layout.register_screen),View.OnClickListener {

    lateinit var loginField:TextInputEditText
    lateinit var passField:TextInputEditText
    lateinit var repeatPassField:TextInputEditText

    lateinit var loginLayout:TextInputLayout
    lateinit var passLayout:TextInputLayout
    lateinit var repeatPassLayout:TextInputLayout

    lateinit var regButton:Button

    lateinit var mViewModel:RegisterViewModel


    companion object{
        fun newInstance():RegisterFragment{
            return RegisterFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel=ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initListeners()
    }

    private fun initViews(view:View){
        loginField=view.findViewById(R.id.textInputLogin)
        passField=view.findViewById(R.id.textInputPass)
        repeatPassField=view.findViewById(R.id.textInputRepPass)

        loginLayout=view.findViewById(R.id.textInputLoginLayout)
        passLayout=view.findViewById(R.id.textInputPassLayout)
        repeatPassLayout=view.findViewById(R.id.textInputRepPassLayout)

        regButton=view.findViewById(R.id.regButton)
    }

    private fun initListeners(){
        regButton.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            regButton.id->{
                mViewModel.registerUser(loginField.text.toString(),passField.text.toString())
            }
        }
    }
}