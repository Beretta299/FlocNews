package com.karasm.flocnews.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.karasm.flocnews.Utils.UtilsClass

class LoginViewModel(app:Application): AndroidViewModel(app) {
    private var fireBaseAuth:FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(email:String,password:String){
        fireBaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Log.d(UtilsClass.RESULT_TAG,"User completely login")
            }.addOnFailureListener {
                Log.d(UtilsClass.RESULT_TAG,"User login error")
            }
    }
}