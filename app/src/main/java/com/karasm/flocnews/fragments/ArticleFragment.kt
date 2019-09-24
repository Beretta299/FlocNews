package com.karasm.flocnews.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.interfaces.iArticleNavigation
import com.karasm.flocnews.interfaces.iDialogReadyCallback
import com.karasm.flocnews.models.NewsNetModel
import com.squareup.picasso.Picasso

class ArticleFragment:DialogFragment(),View.OnClickListener {

    lateinit var articleTitle:TextView
    lateinit var articleText:TextView
    lateinit var articleDate:TextView
    lateinit var articleAuthor:TextView
    lateinit var articleImage:ImageView
    lateinit var closeButton:ImageButton
    lateinit var previousArticle:TextView
    lateinit var nextArticle:TextView
    lateinit var articleSource:TextView
    lateinit var scrollView:ScrollView
    private var currentPosition:Int=0

    lateinit var callBack: iDialogReadyCallback
    lateinit var navigationCallback:iArticleNavigation



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,R.style.FullScreenDialogTheme)
    }

    fun setCallback(callback: iDialogReadyCallback){
        this.callBack=callback
    }

    fun setCurrentPosition(position: Int){
        currentPosition=position
    }

    fun showAllButtons(){
        previousArticle.visibility=View.VISIBLE
        nextArticle.visibility=View.VISIBLE
    }

    fun hidePrevButton(){
        previousArticle.visibility=View.INVISIBLE
    }

    fun hideNextButton(){
        nextArticle.visibility=View.INVISIBLE
    }

    fun getCurrentPosition():Int{
        return currentPosition
    }

    fun setNavCallback(navCallback:iArticleNavigation){
        navigationCallback=navCallback
    }

    companion object{
        fun newInstance(): ArticleFragment {
            val fragment= ArticleFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.full_article_screen,container,false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        callBack.dialogReady()
    }

    fun setArticle(newsNetModel: NewsNetModel){
        articleTitle.text=newsNetModel.title
        articleText.text=newsNetModel.content
        articleAuthor.text=newsNetModel.author
        articleDate.text=newsNetModel.publishedAt
        articleSource.text=newsNetModel.source.name
        if(newsNetModel.urlToImage!=""){
            Picasso.get()
                .load(newsNetModel.urlToImage)
                .error(R.drawable.ic_no_photo)
                .fit()
                .centerCrop()
                .into(articleImage)
        }else{
            Picasso.get()
                .load(R.drawable.ic_no_photo)
                .fit()
                .centerCrop()
                .into(articleImage)
        }
    }



    private fun initViews(view: View) {
        articleTitle=view.findViewById(R.id.articleTitle)
        articleText=view.findViewById(R.id.articleText)
        articleDate=view.findViewById(R.id.articleDate)
        articleAuthor=view.findViewById(R.id.articleAuthor)
        articleImage=view.findViewById(R.id.articleImage)
        closeButton=view.findViewById(R.id.fullscreen_dialog_close)
        previousArticle=view.findViewById(R.id.articlePrev)
        nextArticle=view.findViewById(R.id.articleNext)
        articleSource=view.findViewById(R.id.articleSource)
        scrollView=view.findViewById(R.id.scrollView)
    }
    fun initListeners(){
        closeButton.setOnClickListener(this)
        previousArticle.setOnClickListener(this)
        nextArticle.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.fullscreen_dialog_close->{
                dismiss()
            }
            R.id.articlePrev->{
                navigationCallback.previousArticle()
                scrollView.fullScroll(ScrollView.FOCUS_UP)
            }
            R.id.articleNext->{
                Log.d(UtilsClass.RESULT_TAG,"Next article")
                navigationCallback.nextArticle()
                scrollView.fullScroll(ScrollView.FOCUS_UP)
            }
        }
    }
}