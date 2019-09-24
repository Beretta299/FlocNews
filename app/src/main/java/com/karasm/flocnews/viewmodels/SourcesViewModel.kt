package com.karasm.flocnews.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.models.ServerSourceModel
import com.karasm.flocnews.models.SourceDBModel
import com.karasm.flocnews.models.SourceModel
import com.karasm.flocnews.rest.NewsRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.StringBuilder
import javax.xml.transform.Source

class SourcesViewModel(val app:Application):AndroidViewModel(app) {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var sourcesReference:DatabaseReference=FirebaseDatabase.getInstance().getReference("sources")
    private var sourcesData:MutableLiveData<List<ServerSourceModel>> = MutableLiveData()

    fun observeSources():LiveData<List<ServerSourceModel>>{
        return sourcesData
    }

    fun loadSources(){
        sourcesReference.child(firebaseAuth.uid!!).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(UtilsClass.RESULT_TAG,p0.details+" "+p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                    val serverDBModel:SourceDBModel=p0.getValue(SourceDBModel::class.java)!!
                    val disposable=NewsRepositoryProvider
                        .provideNewsRepository()
                        .getSources(app.getString(R.string.news_api_key),"","")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            val sourcesList=it!!.body()!!.sources
                            val userSourcesList=serverDBModel.sourceId.split(", ")
                            Log.d(UtilsClass.RESULT_TAG,"${userSourcesList.size}")
                            for(userSource in userSourcesList){
                                for((i, source) in sourcesList.withIndex()){
                                    if(userSource==source.id){
                                      sourcesList[i].isChecked=true
                                    }
                                }
                            }
                            sourcesData.value=sourcesList
                        },{
                            Log.d(UtilsClass.RESULT_TAG,it.toString())
                        })
            }
        })
    }

    fun updateSources(list:List<ServerSourceModel>){
        var listOfSources=StringBuilder()
        for((i,sourceModel) in list.withIndex()){
            if(sourceModel.isChecked){
                listOfSources.append("${sourceModel.id}, ")
            }
        }
        if(listOfSources.count()>0){
            listOfSources.delete(listOfSources.count()-2,listOfSources.count())
        }
        sourcesReference.child(firebaseAuth.uid!!).setValue(SourceDBModel(listOfSources.toString()))
    }


}