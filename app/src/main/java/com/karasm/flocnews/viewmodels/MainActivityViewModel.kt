package com.karasm.flocnews.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class MainActivityViewModel(app:Application):AndroidViewModel(app) {
    private var userLogonData:MutableLiveData<Boolean> = MutableLiveData()
    private var firebaseAuth:FirebaseAuth =FirebaseAuth.getInstance()

    fun isUserLogged():LiveData<Boolean>{
        userLogonData.value = firebaseAuth.currentUser!=null
        return userLogonData
    }

    fun logoutUser(){
        firebaseAuth.signOut()
    }


}