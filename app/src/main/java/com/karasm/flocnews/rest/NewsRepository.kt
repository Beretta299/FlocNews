package com.karasm.flocnews.rest

import com.karasm.flocnews.models.NewsModel
import com.karasm.flocnews.models.SourceModel
import io.reactivex.Observable
import retrofit2.Response

class NewsRepository (private val apiService: NewsApiService){
    fun getSources(apiKey:String,category: String,language:String): Observable<Response<SourceModel>> {
        return apiService.getSources(apiKey,category,language)
    }

    fun getNews(apiKey:String,sources:String,category: String,language: String):Observable<Response<NewsModel>>{
        return apiService.getHeadlines(apiKey,sources,category,language)
    }
}