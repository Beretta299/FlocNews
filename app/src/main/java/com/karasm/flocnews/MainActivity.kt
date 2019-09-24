package com.karasm.flocnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.karasm.flocnews.models.CountryModel
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.MenuItem
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.karasm.flocnews.fragments.*
import com.karasm.flocnews.models.CityModel
import com.karasm.flocnews.viewmodels.MainActivityViewModel
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var mDrawerLayout:DrawerLayout
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView:NavigationView
    lateinit var mViewModel:MainActivityViewModel
    lateinit var databaseReference:DatabaseReference
    lateinit var databaseCityReference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        val actionBar=supportActionBar
        actionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        actionBar.setDisplayHomeAsUpEnabled(true)
        mDrawerLayout=findViewById(R.id.activity_main)
        navigationView=findViewById(R.id.nv)
        mDrawerToggle= ActionBarDrawerToggle(this,mDrawerLayout,R.string.app_name,R.string.app_name)
        mViewModel=ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        navigationView.setNavigationItemSelectedListener(this)
        setDrawerState(true)
        setStartFragment()
        mViewModel.preLoadCountries()
        mViewModel.preLoadCities()
        mViewModel.isUserExist().observe(this,androidx.lifecycle.Observer {
            if(it){

            }
        })
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

    fun insertCountries(){
        var id=databaseReference.push().key.toString()
        databaseReference.child(id).setValue(CountryModel(null,"Ukraine"))
        id=databaseReference.push().key.toString()
        databaseReference.child(id).setValue(CountryModel(null,"Poland"))
        id=databaseReference.push().key.toString()
        databaseReference.child(id).setValue(CountryModel(null,"Germany"))
    }

    fun insertCitys(){
        databaseReference.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for((i, model:DataSnapshot) in dataSnapshot.children.withIndex()){
                        if(i==0){
                        for(cityName in resources.getStringArray(R.array.Ukraine)){
                                val id=databaseCityReference.push().key.toString()
                                databaseCityReference.child(id).setValue(CityModel(null,model.key.toString(),cityName))
                            }
                        }else if(i==1){
                            for(cityName in resources.getStringArray(R.array.Poland)){
                                val id=databaseCityReference.push().key.toString()
                                databaseCityReference.child(id).setValue(CityModel(null,model.key.toString(),cityName))
                            }
                        }else if(i==2){
                            for(cityName in resources.getStringArray(R.array.Germany)){
                                val id=databaseCityReference.push().key.toString()
                                databaseCityReference.child(id).setValue(CityModel(null,model.key.toString(),cityName))
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //handle databaseError
                }
            })
    }


private fun setStartFragment(){
mViewModel.isUserLogged().observe(this,androidx.lifecycle.Observer {
    if(it){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,NewsFragment.newInstance())
            .commit()
    }else{
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,LoginFragment.newInstance())
            .commit()
    }
})
}



override fun onNavigationItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
        R.id.news-> {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,NewsFragment.newInstance())
                .commit()
        }
        R.id.sources-> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SourcesFragment.newInstance())
                    .commit()
        }
        R.id.weather-> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,WeatherFragment.newInstance())
                    .commit()
        }
        R.id.profile-> {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,UserDataFragment.newInstance(false))
                .commit()
        }
        R.id.exit-> {
                mViewModel.logoutUser()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,LoginFragment.newInstance())
                    .commit()
        }
    }
    mDrawerLayout.closeDrawer(GravityCompat.START)
    return false
}


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                mDrawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return true
    }
}
