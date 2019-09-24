package com.karasm.flocnews.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.adapters.NewsAdapter
import com.karasm.flocnews.interfaces.iArticleNavigation
import com.karasm.flocnews.interfaces.iDialogReadyCallback
import com.karasm.flocnews.interfaces.iNewsListener
import com.karasm.flocnews.models.NewsNetModel
import com.karasm.flocnews.viewmodels.NewsViewModel



class NewsFragment : Fragment(R.layout.news_screen),SwipeRefreshLayout.OnRefreshListener,
    iNewsListener, View.OnClickListener {

    override fun onRefresh() {
        getNews()
    }

    lateinit var mViewModel:NewsViewModel
    lateinit var rView:RecyclerView
    lateinit var swipeRefresh:SwipeRefreshLayout
    lateinit var adapter:NewsAdapter
    lateinit var warningTextView:TextView


    companion object{
        fun newInstance():NewsFragment{
            return NewsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.show()
        mViewModel= ViewModelProviders.of(this).get(NewsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        initDataObserving()
        getNews()
        swipeRefresh.isRefreshing=true
    }

    private fun initListeners() {
        swipeRefresh.setOnRefreshListener(this)
        warningTextView.setOnClickListener(this)
    }

    private fun initViews(view: View) {
        rView=view.findViewById(R.id.rView)
        swipeRefresh=view.findViewById(R.id.swipeRefresh)
        warningTextView=view.findViewById(R.id.warningTextView)
    }

    fun initDataObserving(){
        mViewModel.getNewsData().observe(this, Observer {
                list->
            if(list==null){
                setSourcesWarning()
            }else{
                hideSourcesWarning()
                initRecyclerView(list)
            }

        })
    }

    fun hideSourcesWarning(){
        warningTextView.visibility=View.GONE
    }

    fun setSourcesWarning(){
        warningTextView.visibility=View.VISIBLE
        swipeRefresh.isRefreshing=false
    }

    fun getNews(){
        mViewModel.getNews()
    }

    private fun initRecyclerView(list:List<NewsNetModel>){
        swipeRefresh.isRefreshing=false
        adapter= NewsAdapter(context!!,list,this)
        val linearLayoutManager=LinearLayoutManager(context!!)
        rView.layoutManager=linearLayoutManager
        rView.adapter=adapter
    }

    override fun onItemClick(newsModel: NewsNetModel,position:Int) {
        val dialog: ArticleFragment =
            ArticleFragment.newInstance()
        dialog.show(fragmentManager!!,NewsFragment::class.java.simpleName)
        dialog.setCallback(object:iDialogReadyCallback{
            override fun dialogReady() {
                dialog.setArticle(newsModel)
                dialog.setCurrentPosition(position)
                if(dialog.getCurrentPosition()==0) dialog.hidePrevButton() else if((dialog.getCurrentPosition()+1>=adapter.list.size)) dialog.hideNextButton() else dialog.showAllButtons()
            }
        })
        dialog.setNavCallback(object:iArticleNavigation{
            override fun nextArticle() {
                if((dialog.getCurrentPosition()+1)<adapter.list.size){
                dialog.setCurrentPosition(dialog.getCurrentPosition()+1)
                dialog.setArticle(adapter.list[dialog.getCurrentPosition()])
                    if(dialog.getCurrentPosition()==0) dialog.hidePrevButton() else if((dialog.getCurrentPosition()+1>=adapter.list.size)) dialog.hideNextButton() else dialog.showAllButtons()
                }
            }

            override fun previousArticle() {
                if((dialog.getCurrentPosition()-1)>=0){
                    dialog.setCurrentPosition(dialog.getCurrentPosition()-1)
                    dialog.setArticle(adapter.list[dialog.getCurrentPosition()])
                    if(dialog.getCurrentPosition()==0) dialog.hidePrevButton() else if((dialog.getCurrentPosition()+1>=adapter.list.size)) dialog.hideNextButton() else dialog.showAllButtons()
                }
            }

        })

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            warningTextView.id->{
                fragmentManager!!.beginTransaction()
                    .replace(R.id.fragment_container,SourcesFragment.newInstance())
                    .commit()
            }
        }
    }


}