package com.karasm.flocnews.models


data class SourceModel(val sources:List<ServerSourceModel>)

data class ServerSourceModel (var isChecked:Boolean=false,val id:String, val name:String, val description:String, val url:String, val category:String, val language:String, val country:String)