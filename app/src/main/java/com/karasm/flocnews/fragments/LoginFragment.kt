package com.karasm.flocnews.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.adapters.ProgressDialog
import com.karasm.flocnews.viewmodels.LoginViewModel
import com.karasm.flocnews.interfaces.iDrawerLocker


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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as iDrawerLocker).toggleDrawer(false)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            logButton.id->{
                val dialog=ProgressDialog(context!!)
                dialog.show()
                mViewModel.loginUser(loginField.text.toString(),passField.text.toString()).observe(this,
                    Observer {
                        dialog.dismiss()
                        if(it){
                            mViewModel.isHasDBRecord().observe(this, Observer {
                                exists->
                                Log.d(UtilsClass.RESULT_TAG,"User $exists")
                                if(exists){
                                fragmentManager!!
                                    .beginTransaction()
                                    .replace(R.id.fragment_container,NewsFragment.newInstance())
                                    .commit()
                                }else{
                                    fragmentManager!!
                                        .beginTransaction()
                                        .replace(R.id.fragment_container,UserDataFragment.newInstance(true))
                                        .commit()
                                }
                            })

                        }else{
                            loginLayout.error=getString(R.string.email_login_error)
                            passLayout.error=getString(R.string.email_login_error)
                        }
                    })

            }
            regButton.id->{
                fragmentManager!!
                    .beginTransaction()
                    .replace(R.id.fragment_container,RegisterFragment.newInstance())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}