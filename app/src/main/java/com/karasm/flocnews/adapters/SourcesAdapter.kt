package com.karasm.flocnews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karasm.flocnews.R
import com.karasm.flocnews.models.ServerSourceModel

class SourcesAdapter(val context: Context, val list:List<ServerSourceModel>): RecyclerView.Adapter<SourcesAdapter.SourcesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourcesViewHolder {
        val inflater=LayoutInflater.from(context)
        val view=inflater.inflate(R.layout.source_template,parent,false)
        return SourcesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: SourcesViewHolder, position: Int) {
        holder.initSource(list[position])
    }


    inner class SourcesViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        lateinit var sourceTitle:TextView
        lateinit var sourceDescription:TextView
        lateinit var sourceCategory:TextView
        lateinit var sourceLanguage:TextView
        lateinit var sourceCheckBox:CheckBox
        init {
            sourceTitle=view.findViewById(R.id.sourceTitle)
            sourceDescription=view.findViewById(R.id.sourceDescription)
            sourceCategory=view.findViewById(R.id.sourceCategory)
            sourceLanguage=view.findViewById(R.id.sourceLanguage)
            sourceCheckBox=view.findViewById(R.id.sourceCheck)
        }

        fun initSource(sourceModel:ServerSourceModel){
            sourceTitle.text=sourceModel.name
            sourceDescription.text=sourceModel.description
            sourceCategory.text=sourceModel.category
            sourceLanguage.text=sourceModel.language
        }

    }
}