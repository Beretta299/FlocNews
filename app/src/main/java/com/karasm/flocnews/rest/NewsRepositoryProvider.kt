package com.karasm.flocnews.rest

object NewsRepositoryProvider {
    fun provideNewsRepository():NewsRepository{
        return NewsRepository(NewsApiService.create())
    }
}