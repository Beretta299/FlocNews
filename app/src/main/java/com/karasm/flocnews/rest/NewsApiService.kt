package com.karasm.flocnews.rest

import com.karasm.flocnews.models.NewsModel
import com.karasm.flocnews.models.SourceModel
import com.karasm.flocnews.models.WeatherData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NewsApiService {
    @GET("sources")
    fun getSources(@Query("apiKey") apiKey:String,@Query("category")category: String,@Query("language") language:String):Observable<Response<SourceModel>>

    @GET("top-headlines")
    fun getHeadlines(@Query("apiKey") apiKey:String,@Query("sources") sources:String,@Query("category")category: String,@Query("language") language:String):Observable<Response<NewsModel>>

    @GET
    fun getWeather(@Url url:String,@Query("access_key") key:String,@Query("query") cityName:String ):Observable<Response<WeatherData>>

    companion object {
        fun create():NewsApiService{
            val retrofit=Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://newsapi.org/v2/")
                .build()
            return retrofit.create(NewsApiService::class.java)
        }
    }


}