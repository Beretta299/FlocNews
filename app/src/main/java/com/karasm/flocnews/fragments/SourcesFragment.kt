package com.karasm.flocnews.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.adapters.SourcesAdapter
import com.karasm.flocnews.interfaces.iSourcesInterfaces
import com.karasm.flocnews.models.ServerSourceModel
import com.karasm.flocnews.viewmodels.SourcesViewModel

class SourcesFragment:Fragment(R.layout.sources_screen),iSourcesInterfaces, View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    lateinit var mViewModel:SourcesViewModel
    lateinit var rView:RecyclerView
    lateinit var adapter:SourcesAdapter
    lateinit var swipeRefresh:SwipeRefreshLayout
    companion object{
        fun newInstance():SourcesFragment{
            return SourcesFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel=ViewModelProviders.of(this).get(SourcesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        observeSources()
        initListeners()
        mViewModel.loadSources()
        swipeRefresh.isRefreshing=true
    }

    fun initListeners(){
        swipeRefresh.setOnRefreshListener(this)
    }

    fun initViews(view:View){
        rView=view.findViewById(R.id.rView)
        swipeRefresh=view.findViewById(R.id.swipeRefresh)
    }

    fun observeSources(){
        mViewModel.observeSources().observe(this, Observer {
                list->
            Log.d(UtilsClass.RESULT_TAG,"${list.size}")
            initRecyclerView(list!!)
        })

    }

    private fun initRecyclerView(list: List<ServerSourceModel>) {
        adapter= SourcesAdapter(context!!,list,this)
        val linearLayoutManager=LinearLayoutManager(context!!)
        val dividerItemDecoration=DividerItemDecoration(rView.context,linearLayoutManager.orientation)
        if(rView.itemDecorationCount>=1){
            for(i:Int in 0 until rView.itemDecorationCount-1){
                Log.d(UtilsClass.RESULT_TAG,"item decoration $i ${rView.itemDecorationCount}")
                rView.removeItemDecorationAt(i)
            }
        }
        rView.addItemDecoration(dividerItemDecoration)
        rView.layoutManager=linearLayoutManager
        rView.adapter=adapter
        swipeRefresh.isRefreshing=false
    }

    override fun checkedChange(view: View, position: Int, state: Boolean) {

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.sourceArea->{
                mViewModel.updateSources(adapter.list)
            }
        }
    }

    override fun onRefresh() {
        Log.d(UtilsClass.RESULT_TAG,"Refresh")
        mViewModel.loadSources()
    }
}