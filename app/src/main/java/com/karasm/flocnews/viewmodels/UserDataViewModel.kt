package com.karasm.flocnews.viewmodels

import android.app.Application
import android.service.autofill.UserData
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.models.CityModel
import com.karasm.flocnews.models.CountryModel
import com.karasm.flocnews.R
import com.karasm.flocnews.db.LocalStorage
import com.karasm.flocnews.models.UserModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserDataViewModel(private val app:Application):AndroidViewModel(app) {

    private var countryReference:DatabaseReference = FirebaseDatabase.getInstance().getReference("countries")
    private var cityReference:DatabaseReference = FirebaseDatabase.getInstance().getReference("cities")
    private var userReference:DatabaseReference=FirebaseDatabase.getInstance().getReference("userdata")
    private var firebaseAuth:FirebaseAuth=FirebaseAuth.getInstance()
    private var countryData: MutableLiveData<List<CountryModel>> = MutableLiveData()
    private var isPassCorrect:MutableLiveData<Boolean> =MutableLiveData()
    private var cityData: MutableLiveData<List<CityModel>> = MutableLiveData()
    private var userData: MutableLiveData<UserModel> = MutableLiveData()
    private var isUserExists: MutableLiveData<Boolean> = MutableLiveData()
    private var registeredData:MutableLiveData<Boolean> = MutableLiveData()

    fun getCountries():LiveData<List<CountryModel>>{
        val countriesDisposable=LocalStorage
            .getInstance(app)!!.
            localStorageDataDao().
            getCountries().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                countryData.value=it
            },{
                Log.d(UtilsClass.RESULT_TAG,it.localizedMessage!!)
            })
        return countryData
    }


    fun isPassCorrectObserve():LiveData<Boolean>{
        return isPassCorrect
    }


    fun saveUserData(user:UserModel):LiveData<Boolean>{
        userReference.child(firebaseAuth.currentUser!!.uid).setValue(user)
            .addOnSuccessListener {
                registeredData.value=Boolean.equals(true)
            }
        return registeredData
    }

    fun changePassword(oldPassword:String,newPassword:String){
        Log.d(UtilsClass.RESULT_TAG,"Clicked")
        val authCredential= EmailAuthProvider.getCredential(firebaseAuth.currentUser!!.email!!,oldPassword)
        firebaseAuth.currentUser!!.reauthenticate(authCredential)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    firebaseAuth.currentUser!!.updatePassword(newPassword).addOnCompleteListener {
                        res->
                        Log.d(UtilsClass.RESULT_TAG,"Mi syuda zashli successful")
                        if(res.isSuccessful){
                            isPassCorrect.value=true
                        }
                    }
                }
            }.addOnFailureListener {
                isPassCorrect.value=false
            }
    }

    fun isUserExists():LiveData<Boolean>{
        if(firebaseAuth.currentUser==null){
            isUserExists.value=false
            return isUserExists
        }
        userReference.child(firebaseAuth.uid!!).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                isUserExists.value = p0.exists()
            }
        })
        return isUserExists
    }

    fun getUserData():LiveData<UserModel>{
        userReference.child(firebaseAuth.uid!!).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val userModel:UserModel=p0.getValue(UserModel::class.java)!!
                userData.value=userModel
            }
        })
        return userData
    }

    fun getCities(key:String):LiveData<List<CityModel>>{
        val cityDisposable=LocalStorage.getInstance(app)!!
            .localStorageDataDao()
            .getCities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val neededList=ArrayList<CityModel>()
                for(cityModel in it){
                    if(cityModel.countryId==key){
                        neededList.add(cityModel)
                    }
                }
                cityData.value=neededList
            },{
                Log.d(UtilsClass.RESULT_TAG,it.localizedMessage!!)
            })
        return cityData
    }
}