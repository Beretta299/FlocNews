package com.karasm.flocnews.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.adapters.NewsAdapter
import com.karasm.flocnews.models.NewsNetModel
import com.karasm.flocnews.viewmodels.NewsViewModel



class NewsFragment : Fragment(R.layout.news_screen),SwipeRefreshLayout.OnRefreshListener{
    override fun onRefresh() {
        getNews()
    }

    lateinit var mViewModel:NewsViewModel
    lateinit var rView:RecyclerView
    lateinit var swipeRefresh:SwipeRefreshLayout



    companion object{
        fun newInstance():NewsFragment{
            return NewsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel= ViewModelProviders.of(this).get(NewsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rView=view.findViewById(R.id.rView)
        swipeRefresh=view.findViewById(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener(this)


        initDataObserving()
        getNews()
    }

    fun initDataObserving(){
        mViewModel.getNewsData().observe(this, Observer {
                list->
            Log.d(UtilsClass.RESULT_TAG,"We are here")
            initRecyclerView(list)
        })
    }

    fun getNews(){
        mViewModel.loadNews()
    }

    private fun initRecyclerView(list:List<NewsNetModel>){
        swipeRefresh.isRefreshing=false
        val adapter= NewsAdapter(context!!,list)
        val linearLayoutManager=LinearLayoutManager(context!!)
        rView.layoutManager=linearLayoutManager
        rView.adapter=adapter
    }




}