package com.karasm.flocnews.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.models.ServerSourceModel
import com.karasm.flocnews.models.SourceModel
import com.karasm.flocnews.rest.NewsRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SourcesViewModel(val app:Application):AndroidViewModel(app) {
    private var sourcesData:MutableLiveData<List<ServerSourceModel>> = MutableLiveData()

    fun loadSources():LiveData<List<ServerSourceModel>>{
        val disposable=NewsRepositoryProvider
            .provideNewsRepository()
            .getSources(app.getString(R.string.news_api_key),"","")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                sourcesData.value=it!!.body()!!.sources
            },{
                Log.d(UtilsClass.RESULT_TAG,it.toString())
            })
        return sourcesData
    }


}