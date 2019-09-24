package com.karasm.flocnews.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.viewmodels.RegisterViewModel

class RegisterFragment: Fragment(R.layout.register_screen),View.OnClickListener,TextWatcher {

    lateinit var loginField:TextInputEditText
    lateinit var passField:TextInputEditText
    lateinit var repeatPassField:TextInputEditText

    lateinit var loginLayout:TextInputLayout
    lateinit var passLayout:TextInputLayout
    lateinit var repeatPassLayout:TextInputLayout

    lateinit var regButton:Button

    lateinit var mViewModel:RegisterViewModel
    lateinit var toolbar: Toolbar

    lateinit var layoutArray:ArrayList<TextInputLayout>
    lateinit var fieldsArray:ArrayList<TextInputEditText>
    var passText=""
    var passRepeatText=""


    companion object{
        fun newInstance():RegisterFragment{
            return RegisterFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        mViewModel=ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initListeners()
        initLayoutArray()
    }

    private fun initLayoutArray(){
        layoutArray=ArrayList()
        layoutArray.add(loginLayout)
        layoutArray.add(passLayout)
        layoutArray.add(repeatPassLayout)
        fieldsArray= ArrayList()
        fieldsArray.add(loginField)
        fieldsArray.add(passField)
        fieldsArray.add(repeatPassField)
    }

    private fun initViews(view:View){
        loginField=view.findViewById(R.id.textInputEmail)
        passField=view.findViewById(R.id.textInputPass)
        repeatPassField=view.findViewById(R.id.textInputRepPass)

        loginLayout=view.findViewById(R.id.textInputEmailLayout)
        passLayout=view.findViewById(R.id.textInputPassLayout)
        repeatPassLayout=view.findViewById(R.id.textInputRepPassLayout)
        toolbar=view.findViewById(R.id.toolbarId)

        regButton=view.findViewById(R.id.regButton)
        toolbar.setNavigationOnClickListener {
            fragmentManager!!.popBackStack()
        }
    }

    private fun initListeners(){
        regButton.setOnClickListener(this)
        loginField.addTextChangedListener(this)
        passField.addTextChangedListener(this)
        repeatPassField.addTextChangedListener(this)
    }

    fun isNoError():Boolean{
        var boolean=true
        for(layout in layoutArray){
            if(layout.error!=null){
                boolean=false
                break
            }
        }
        for((i,field) in fieldsArray.withIndex()){
            if(field.text.toString()==""){
                layoutArray[i].error=getString(R.string.empty_field)
                boolean=false
            }
        }

    return boolean
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            regButton.id->{
                if(isNoError()){
                mViewModel.registerUser(loginField.text.toString(),passField.text.toString())
                fragmentManager!!.beginTransaction()
                    .replace(R.id.fragment_container,UserDataFragment.newInstance(true))
                    .commit()
                }
            }
        }
    }


    override fun afterTextChanged(p0: Editable?) {
        if(passText!=passRepeatText){
            repeatPassLayout.error=getString(R.string.pass_not_match)
        }else{
            repeatPassLayout.error=""
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        passText=passField.text!!.trim().toString()
        passRepeatText=repeatPassField.text!!.trim().toString()
        when(p0.hashCode()){
            loginField.text.hashCode()->{
                if(!loginField.text!!.matches(android.util.Patterns.EMAIL_ADDRESS.toRegex())){
                    loginLayout.error=getString(R.string.email_input_error)
                }else{
                    loginLayout.error=""
                }
            }
            passField.text.hashCode()->{
                if(!passField.text!!.matches(Regex(UtilsClass.PASSWORD_REGEX))){
                    passLayout.error=getString(R.string.pass_error)
                }else{
                    passLayout.error=""
                }
            }
        }
    }
}