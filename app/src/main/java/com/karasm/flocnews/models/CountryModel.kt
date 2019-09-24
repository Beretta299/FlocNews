package com.karasm.flocnews.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CountryModel(@PrimaryKey(autoGenerate = true) var id:Long?=null, var keyValue:String="", var countryName:String="")