package com.karasm.flocnews.models

data class WeatherData(val current:WeatherModel)

data class WeatherModel(var cityName:String="",val observation_time:String,val humidity:Int,val temperature:Int,val weather_descriptions:List<String>,val wind_speed:Int,val wind_dir:String,val feelslike:Int)