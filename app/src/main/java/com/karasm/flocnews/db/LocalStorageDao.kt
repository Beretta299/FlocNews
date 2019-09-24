package com.karasm.flocnews.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.karasm.flocnews.models.CityModel
import com.karasm.flocnews.models.CountryModel
import com.karasm.flocnews.models.WeatherModel
import io.reactivex.Flowable
import org.intellij.lang.annotations.Flow

@Dao
interface LocalStorageDao {
    @Insert
    fun insertCountries(countryList:List<CountryModel>)

    @Query("select count(*) from CountryModel")
    fun countOfTheCountries():Flowable<Int>

    @Query("select count(*) from CityModel")
    fun countOfTheCities():Flowable<Int>

    @Query("delete from CountryModel")
    fun deleteCountries()

    @Query("select * from CountryModel")
    fun getCountries():Flowable<List<CountryModel>>

    @Query("select * from CityModel")
    fun getCities():Flowable<List<CityModel>>

    @Insert
    fun insertCitys(cityList:List<CityModel>)

    @Query("delete from CityModel")
    fun deleteCitys()
}