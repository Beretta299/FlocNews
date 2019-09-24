package com.karasm.flocnews.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.models.WeatherModel
import com.karasm.flocnews.viewmodels.WeatherViewModel
import java.lang.StringBuilder

class WeatherFragment:Fragment(R.layout.weather_screen),SwipeRefreshLayout.OnRefreshListener {

    lateinit var weatherTemperature:TextView
    lateinit var weatherFeelsLike:TextView
    lateinit var weatherWindDirection:TextView
    lateinit var weatherDescription:TextView
    lateinit var weatherWindSpeed:TextView
    lateinit var weatherHumidity:TextView
    lateinit var weatherCityName:TextView
    lateinit var mViewModel:WeatherViewModel
    //lateinit var swipeRefresh:SwipeRefreshLayout

    companion object{
        fun newInstance():WeatherFragment{
            return WeatherFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel=ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        getWeather()
    }

    fun setWeatherData(weather:WeatherModel){
        weatherCityName.text=weather.cityName
        weatherHumidity.text=String.format(getString(R.string.humidity_postfix),weather.humidity,"%")
       weatherWindSpeed.text= String.format(getString(R.string.wind_postfix),weather.wind_speed)
        val builder=StringBuilder()
        for(description in weather.weather_descriptions){
            builder.append(description+" ")
        }
        weatherDescription.text=builder.toString()
        weatherWindDirection.text=String.format(getString(R.string.wind_direction),weather.wind_dir)
        weatherFeelsLike.text=String.format(getString(R.string.feels_like,weather.feelslike))
        weatherTemperature.text=String.format(getString(R.string.temperature_postfix,weather.temperature))
    }


    private fun getWeather() {
        mViewModel.observeWeather().observe(this, Observer {
            Log.d(UtilsClass.RESULT_TAG,"Current temperature ${it.temperature}")
            setWeatherData(it)
        })
        mViewModel.loadWeather()
    }

    private fun initViews(view: View) {
        weatherTemperature=view.findViewById(R.id.weatherTemperature)
        weatherFeelsLike=view.findViewById(R.id.weatherFeelsLike)
        weatherWindDirection=view.findViewById(R.id.weatherWindDirection)
        weatherDescription=view.findViewById(R.id.weatherDescription)
        weatherWindSpeed=view.findViewById(R.id.weatherWindSpeed)
        weatherHumidity=view.findViewById(R.id.weatherHumidity)
        weatherCityName=view.findViewById(R.id.weatherCityName)
        //swipeRefresh=view.findViewById(R.id.swipeRefresh)
    }

    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}