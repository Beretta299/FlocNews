package com.karasm.flocnews.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.db.LocalStorage
import com.karasm.flocnews.models.CityModel
import com.karasm.flocnews.models.CountryModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(val app:Application):AndroidViewModel(app) {
    private var userLogonData:MutableLiveData<Boolean> = MutableLiveData()
    private var firebaseAuth:FirebaseAuth =FirebaseAuth.getInstance()
    private var countryReference:DatabaseReference=FirebaseDatabase.getInstance().getReference("countries")
    private var cityReference:DatabaseReference=FirebaseDatabase.getInstance().getReference("cities")
    private var disposeBag=CompositeDisposable()

    fun isUserLogged():LiveData<Boolean>{
        userLogonData.value = firebaseAuth.currentUser!=null
        return userLogonData
    }

    fun preLoadCities(){
        cityReference
        cityReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val list:ArrayList<CityModel> = ArrayList()
                for(modelSnap in p0.children){
                    val cityModel:CityModel=modelSnap.getValue(CityModel::class.java)!!
                    cityModel.keyValue=modelSnap.key.toString()
                    list.add(cityModel)
                }
                val countDisposable=LocalStorage.getInstance(app)!!.localStorageDataDao().countOfTheCities()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if(it<list.size){
                            saveCitiesToLocalStoreage(list)
                        }
                    },{Log.d(UtilsClass.RESULT_TAG,it.localizedMessage!!)})
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })
    }

    fun preLoadCountries(){
        countryReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val list:ArrayList<CountryModel> = ArrayList()
                for(modelSnap in p0.children){
                    var countryModel: CountryModel =modelSnap.getValue(CountryModel::class.java)!!
                    countryModel.keyValue=modelSnap.key.toString()
                    list.add(countryModel)
                }
                val disposable=LocalStorage.getInstance(app)!!.localStorageDataDao().countOfTheCountries().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        result->
                        if(result<list.size){
                            saveCountriesToLocalStorage(list)
                        }
                    },{error->
                        Log.d(UtilsClass.RESULT_TAG,error.localizedMessage!!)
                    })
                disposeBag.add(disposable)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun saveCountriesToLocalStorage(list: ArrayList<CountryModel>) {
        Single.fromCallable {
            LocalStorage.getInstance(app)!!.localStorageDataDao().deleteCountries()
            LocalStorage.getInstance(app)!!.localStorageDataDao().insertCountries(list) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun saveCitiesToLocalStoreage(list:ArrayList<CityModel>){
        Single.fromCallable {
            LocalStorage.getInstance(app)!!.localStorageDataDao().deleteCitys()
            LocalStorage.getInstance(app)!!.localStorageDataDao().insertCitys(list)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun logoutUser(){
        firebaseAuth.signOut()
    }

    override fun onCleared() {
        super.onCleared()
        disposeBag.clear()
    }
}