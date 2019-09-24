package com.karasm.flocnews.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.karasm.flocnews.Utils.UtilsClass

class RegisterViewModel(app:Application):AndroidViewModel(app) {
    lateinit var firebaseAuth : FirebaseAuth

    init {
        firebaseAuth= FirebaseAuth.getInstance()
    }

    fun registerUser(email:String,password:String){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Log.d(UtilsClass.RESULT_TAG,"Registration completed")
            }.addOnFailureListener {
                Log.d(UtilsClass.RESULT_TAG,it.toString())
            }
    }

}