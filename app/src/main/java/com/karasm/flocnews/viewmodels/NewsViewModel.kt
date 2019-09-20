package com.karasm.flocnews.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.models.NewsNetModel
import com.karasm.flocnews.rest.NewsRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsViewModel(val app:Application) : AndroidViewModel(app) {
    var mNewsListData:MutableLiveData<List<NewsNetModel>>?=null

    fun getNewsData():MutableLiveData<List<NewsNetModel>>{
        if(mNewsListData==null){
            mNewsListData= MutableLiveData()
        }
        return mNewsListData!!
    }

    fun loadNews(){
        if(mNewsListData==null){
            mNewsListData= MutableLiveData()
        }
        val disposable=NewsRepositoryProvider.provideNewsRepository()
            .getNews(app.resources.getString(R.string.news_api_key),"the-verge","","")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    res->
                Log.d(UtilsClass.RESULT_TAG,"${res.code()}")
                mNewsListData!!.value=res.body()!!.articles
            },{
                Log.d(UtilsClass.RESULT_TAG,it.localizedMessage);
            })
    }

}