package com.karasm.flocnews.interfaces

import com.karasm.flocnews.models.NewsNetModel

interface iNewsListener {
    fun onItemClick(newsModel : NewsNetModel,position:Int)
}