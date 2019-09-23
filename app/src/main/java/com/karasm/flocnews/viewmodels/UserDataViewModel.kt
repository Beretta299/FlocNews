package com.karasm.flocnews.viewmodels

import android.app.Application
import android.service.autofill.UserData
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.models.CityModel
import com.karasm.flocnews.models.CountryModel
import com.karasm.flocnews.R
import com.karasm.flocnews.models.UserModel

class UserDataViewModel(private val app:Application):AndroidViewModel(app) {

    private var countryReference:DatabaseReference = FirebaseDatabase.getInstance().getReference("countries")
    private var cityReference:DatabaseReference = FirebaseDatabase.getInstance().getReference("cities")
    private var userReference:DatabaseReference=FirebaseDatabase.getInstance().getReference("userdata")
    private var firebaseAuth:FirebaseAuth=FirebaseAuth.getInstance()
    private var countryData: MutableLiveData<List<CountryModel>> = MutableLiveData()
    private var cityData: MutableLiveData<List<CityModel>> = MutableLiveData()
    private var userData: MutableLiveData<UserModel> = MutableLiveData()
    private var isUserExists: MutableLiveData<Boolean> = MutableLiveData()
    private var registeredData:MutableLiveData<Boolean> = MutableLiveData()

    fun getCountries():LiveData<List<CountryModel>>{

        countryReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val list:ArrayList<CountryModel> = ArrayList()
                for(modelSnap in p0.children){
                    var countryModel:CountryModel=modelSnap.getValue(CountryModel::class.java)!!
                    countryModel.keyValue=modelSnap.key.toString()
                    list.add(countryModel)
                }
                countryData.value=list
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        return countryData
    }


    fun saveUserData(user:UserModel):LiveData<Boolean>{
        userReference.child(firebaseAuth.currentUser!!.uid).setValue(user)
            .addOnSuccessListener {
                registeredData.value=Boolean.equals(true)
            }
        return registeredData
    }


    fun isUserExists():LiveData<Boolean>{
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
        val query:Query=cityReference.orderByChild("countryId").equalTo(key)
        query.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val list:ArrayList<CityModel> = ArrayList()
                for(modelSnap in p0.children){
                    val cityModel:CityModel=modelSnap.getValue(CityModel::class.java)!!
                    Log.d(UtilsClass.RESULT_TAG,cityModel.cityName)
                    cityModel.keyValue=modelSnap.key.toString()
                    list.add(cityModel)
                }
                cityData.value=list
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })
        return cityData
    }
}