package com.karasm.flocnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.rest.NewsRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.sources_screen.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.karasm.flocnews.fragments.LoginFragment
import com.karasm.flocnews.fragments.NewsFragment
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.karasm.flocnews.fragments.RegisterFragment
import com.karasm.flocnews.models.CountryModel
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.karasm.flocnews.models.CityModel
import java.util.*


class MainActivity : AppCompatActivity(){

    lateinit var mDrawerLayout:DrawerLayout
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    lateinit var databaseReference:DatabaseReference
    lateinit var databaseCityReference:DatabaseReference

    lateinit var countryList:List<CountryModel>
    lateinit var cityList:List<CityModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        mDrawerLayout=findViewById(R.id.activity_main)
        mDrawerToggle= ActionBarDrawerToggle(this,mDrawerLayout,R.string.app_name,R.string.app_name)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,LoginFragment.newInstance())
            .commit()

        setDrawerState(false)

        databaseReference=FirebaseDatabase.getInstance().getReference("countries")
        databaseCityReference=FirebaseDatabase.getInstance().getReference("cities")
    }

    fun setDrawerState(isEnabled: Boolean) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            mDrawerToggle.isDrawerIndicatorEnabled=true
            mDrawerToggle.syncState()
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            mDrawerToggle.isDrawerIndicatorEnabled=false
            mDrawerToggle.syncState()
        }
    }

    fun getCountriesList(){
        databaseReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for(dataSnapshot:DataSnapshot in p0.children){
                    val countryModel:CountryModel=dataSnapshot.getValue(CountryModel::class.java)!!

                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

//    fun insertCountries(){
//        var id=databaseReference.push().key.toString()
//        databaseReference.child(id).setValue(CountryModel("Ukraine"))
//        id=databaseReference.push().key.toString()
//        databaseReference.child(id).setValue(CountryModel("Poland"))
//        id=databaseReference.push().key.toString()
//        databaseReference.child(id).setValue(CountryModel("Germany"))
//    }
//
//    fun insertCitys(){
//        databaseReference.addListenerForSingleValueEvent(
//            object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    for((i, model:DataSnapshot) in dataSnapshot.children.withIndex()){
//                        if(i==0){
//                        for(cityName in resources.getStringArray(R.array.Ukraine)){
//                                val id=databaseCityReference.push().key.toString()
//                                databaseCityReference.child(id).setValue(CityModel(model.key.toString(),cityName))
//                            }
//                        }else if(i==1){
//                            for(cityName in resources.getStringArray(R.array.Poland)){
//                                val id=databaseCityReference.push().key.toString()
//                                databaseCityReference.child(id).setValue(CityModel(model.key.toString(),cityName))
//                            }
//                        }else if(i==2){
//                            for(cityName in resources.getStringArray(R.array.Germany)){
//                                val id=databaseCityReference.push().key.toString()
//                                databaseCityReference.child(id).setValue(CityModel(model.key.toString(),cityName))
//                            }
//                        }
//                    }
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    //handle databaseError
//                }
//            })
//    }
}
