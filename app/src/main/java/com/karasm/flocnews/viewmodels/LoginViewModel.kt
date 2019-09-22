package com.karasm.flocnews.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.karasm.flocnews.Utils.UtilsClass

class LoginViewModel(app:Application): AndroidViewModel(app) {
    private var fireBaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private var isUserLogged:MutableLiveData<Boolean> = MutableLiveData()

    fun loginUser(email:String,password:String):LiveData<Boolean>{
        fireBaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                isUserLogged.value=true
            }.addOnFailureListener {
                isUserLogged.value=false
            }
        return isUserLogged
    }


}