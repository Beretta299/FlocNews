package com.karasm.flocnews.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.karasm.flocnews.Utils.UtilsClass

class LoginViewModel(app:Application): AndroidViewModel(app) {
    private var fireBaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private var isUserLogged:MutableLiveData<Boolean> = MutableLiveData()
    private var hasDBRecord:MutableLiveData<Boolean> = MutableLiveData()
    private var dataBaseReference: DatabaseReference=FirebaseDatabase.getInstance().getReference("userdata")

    fun loginUser(email:String,password:String):LiveData<Boolean>{
        fireBaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                isUserLogged.value=true
            }.addOnFailureListener {
                isUserLogged.value=false
            }
        return isUserLogged
    }

    fun isHasDBRecord():LiveData<Boolean>{
        dataBaseReference.child(fireBaseAuth.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    hasDBRecord.value=p0.exists()
                }
            })

        return hasDBRecord
    }


}