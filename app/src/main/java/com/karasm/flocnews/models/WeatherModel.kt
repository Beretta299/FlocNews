package com.karasm.flocnews.models

import androidx.room.Ignore

data class WeatherData(val current:WeatherModel)

data class WeatherModel(var cityName:String="", val observation_time:String="", val humidity:Int=0, val temperature:Int=0, @Ignore val weather_descriptions:List<String>, val wind_speed:Int=0, val wind_dir:String="", val feelslike:Int=0)