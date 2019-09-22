package com.karasm.flocnews.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karasm.flocnews.R
import com.karasm.flocnews.adapters.SourcesAdapter
import com.karasm.flocnews.viewmodels.SourcesViewModel

class SourcesFragment:Fragment(R.layout.sources_screen) {

    lateinit var mViewModel:SourcesViewModel
    lateinit var rView:RecyclerView

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
            val adapter= SourcesAdapter(context!!,list)
            val linearLayoutManager=LinearLayoutManager(context!!)
            rView.layoutManager=linearLayoutManager
            rView.adapter=adapter
        })

    }
}