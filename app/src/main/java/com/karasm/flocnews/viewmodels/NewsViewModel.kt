package com.karasm.flocnews.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.models.NewsNetModel
import com.karasm.flocnews.models.ServerSourceModel
import com.karasm.flocnews.models.SourceDBModel
import com.karasm.flocnews.rest.NewsRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.StringBuilder

class NewsViewModel(val app:Application) : AndroidViewModel(app) {
    private final val COUNT_OF_SOURCES_START:Int=5
    private var mNewsListData:MutableLiveData<List<NewsNetModel>>?=null
    val firebaseAuth:FirebaseAuth=FirebaseAuth.getInstance()
    var sourcesReference:DatabaseReference=FirebaseDatabase.getInstance().getReference("sources")
    var disposeBag=CompositeDisposable()

    fun getNewsData():MutableLiveData<List<NewsNetModel>>{
        if(mNewsListData==null){
            mNewsListData= MutableLiveData()
        }
        return mNewsListData!!
    }




    fun isSourcesExists(){
        sourcesReference.child(firebaseAuth.uid!!).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(!p0.exists()){
                    Log.d(UtilsClass.RESULT_TAG,"Exists")
                    uploadSources()
                }else{
                    Log.d(UtilsClass.RESULT_TAG,"Not exists")
                }
            }
        })
    }

    fun uploadSources(){
        val disposable=NewsRepositoryProvider.provideNewsRepository()
            .getSources(app.resources.getString(R.string.news_api_key),"","")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val stringBuilder=StringBuilder()
                for((i, model) in it!!.body()!!.sources.withIndex()){
                    if(i%COUNT_OF_SOURCES_START==0&&i!=0){
                        stringBuilder.append(model.id)
                        break
                    }else{
                        stringBuilder.append("${model.id}, ")
                    }

                }
                val sourceModel=SourceDBModel(stringBuilder.toString())
                sourcesReference.child(firebaseAuth.uid!!).setValue(sourceModel)

                Log.d(UtilsClass.RESULT_TAG,stringBuilder.toString())
            },{

            })
        disposeBag.add(disposable)
    }




    fun loadNews(){
        isSourcesExists()
        sourcesReference.child(firebaseAuth.uid!!).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                val sources:SourceDBModel=p0.getValue(SourceDBModel::class.java)!!
                Log.d(UtilsClass.RESULT_TAG,sources.sourceId)
                val disposable=NewsRepositoryProvider.provideNewsRepository()
                    .getNews(app.resources.getString(R.string.news_api_key),sources.sourceId,"","")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                            res->
                        Log.d(UtilsClass.RESULT_TAG,"${res.code()}")
                        mNewsListData!!.value=res.body()!!.articles
                    },{
                        Log.d(UtilsClass.RESULT_TAG,it.localizedMessage);
                    })
                disposeBag.add(disposable)
            }
        })
    }


    override fun onCleared() {
        super.onCleared()
        disposeBag.clear()
    }
}