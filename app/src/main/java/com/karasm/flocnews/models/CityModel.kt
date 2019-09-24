package com.karasm.flocnews.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityModel (@PrimaryKey(autoGenerate = true) var id:Long?=null, var keyValue:String="", val countryId:String="", val cityName:String="")