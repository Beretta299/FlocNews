package com.karasm.flocnews.adapters

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.models.NewsNetModel
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class NewsAdapter(val context: Context, val list:List<NewsNetModel>): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.news_template,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.setNews(list[position])
    }


    inner class NewsViewHolder(val view: View):RecyclerView.ViewHolder(view){
        var newsImage:ImageView
        var newsTitle:TextView
        var newsDateTime:TextView
        var newsSource:TextView

        init {
            newsImage=view.findViewById(R.id.newsImage)
            newsTitle=view.findViewById(R.id.newsTitle)
            newsDateTime=view.findViewById(R.id.newsDateTime)
            newsSource=view.findViewById(R.id.newsSource)
        }
        fun setNews(newsNetModel: NewsNetModel){

            Picasso.get()
                .load(newsNetModel.urlToImage)
                .error(R.drawable.ic_no_photo)
                .fit()
                .centerCrop()
                .into(newsImage)


            newsTitle.text=newsNetModel.title
            newsDateTime.text=newsNetModel.publishedAt
            newsSource.text=newsNetModel.author
        }
    }
}