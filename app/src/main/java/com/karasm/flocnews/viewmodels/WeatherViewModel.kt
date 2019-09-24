package com.karasm.flocnews.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.models.CityModel
import com.karasm.flocnews.models.UserModel
import com.karasm.flocnews.models.WeatherModel
import com.karasm.flocnews.rest.NewsRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WeatherViewModel(val app:Application):AndroidViewModel(app) {
    var firebaseAuth:FirebaseAuth=FirebaseAuth.getInstance()

    var userDataReference:DatabaseReference=FirebaseDatabase.getInstance().getReference("userdata")
    val cityReference:DatabaseReference=FirebaseDatabase.getInstance().getReference("cities")

    var weatherData:MutableLiveData<WeatherModel> = MutableLiveData()



    fun getUserData(){
        userDataReference.child(firebaseAuth.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val userData:UserModel=p0.getValue(UserModel::class.java)!!
                    getCityData(userData.cityId)
                }
            })
    }



    fun getCityData(cityId:String){
        cityReference.child(cityId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val cityValue:CityModel=p0.getValue(CityModel::class.java)!!
                getWeather(cityValue.cityName)
                Log.d(UtilsClass.RESULT_TAG,cityValue.cityName)
            }
        })
    }

    fun observeWeather():LiveData<WeatherModel>{
        return weatherData
    }

    fun loadWeather(){
        getUserData()
    }

    fun getWeather(cityName:String){
        with(app.resources){
        NewsRepositoryProvider.provideNewsRepository()
            .getWeather(getString(R.string.weather_url),getString(R.string.weather_key),cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val weather=it.body()!!.current
                weather.cityName=cityName
            weatherData.value=weather

                Log.d(UtilsClass.RESULT_TAG, " Code of request ${it.code()}")
            },{
            Log.d(UtilsClass.RESULT_TAG,it.localizedMessage!!)
            })
        }
    }
}