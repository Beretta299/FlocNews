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
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.adapters.SourcesAdapter
import com.karasm.flocnews.interfaces.iSourcesInterfaces
import com.karasm.flocnews.viewmodels.SourcesViewModel

class SourcesFragment:Fragment(R.layout.sources_screen),iSourcesInterfaces, View.OnClickListener {

    lateinit var mViewModel:SourcesViewModel
    lateinit var rView:RecyclerView
    lateinit var adapter:SourcesAdapter
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
        rView=view.findViewById(R.id.rView)

        initRecycler()
    }
    fun initRecycler(){
        mViewModel.loadSources().observe(this, Observer {
                list->
            adapter= SourcesAdapter(context!!,list,this)
            val linearLayoutManager=LinearLayoutManager(context!!)
            val dividerItemDecoration=DividerItemDecoration(rView.context,linearLayoutManager.orientation)
            rView.addItemDecoration(dividerItemDecoration)
            rView.layoutManager=linearLayoutManager
            rView.adapter=adapter
        })

    }

    override fun checkedChange(view: View, position: Int, state: Boolean) {
        Log.d(UtilsClass.RESULT_TAG,"$position $state")
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.sourceArea->{
                mViewModel.updateSources(adapter.list)
            }
        }
    }
}