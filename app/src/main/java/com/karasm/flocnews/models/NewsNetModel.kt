package com.karasm.flocnews.models

import org.json.JSONObject
import java.io.Serializable

data class NewsNetModel(val source:ServerSourceModel,val author:String,val title:String,val description:String,val url:String,val urlToImage:String,val publishedAt:String,val content:String):Serializable

data class NewsModel(val articles:List<NewsNetModel>)