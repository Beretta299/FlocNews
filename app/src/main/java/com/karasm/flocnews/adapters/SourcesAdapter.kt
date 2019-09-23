package com.karasm.flocnews.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.interfaces.iSourcesInterfaces
import com.karasm.flocnews.models.ServerSourceModel

class SourcesAdapter(val context: Context, val list:List<ServerSourceModel>,val listener:View.OnClickListener): RecyclerView.Adapter<SourcesAdapter.SourcesViewHolder>() {
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


    inner class SourcesViewHolder(val view: View): RecyclerView.ViewHolder(view),View.OnClickListener{

        var sourceTitle:TextView
        var sourceDescription:TextView
        var sourceCategory:TextView
        var sourceLanguage:TextView
        var sourceCheckBox:CheckBox
        var sourceRegion:TextView
        init {
            sourceTitle=view.findViewById(R.id.sourceTitle)
            sourceDescription=view.findViewById(R.id.sourceDescription)
            sourceCategory=view.findViewById(R.id.sourceCategory)
            sourceLanguage=view.findViewById(R.id.sourceLanguage)
            sourceCheckBox=view.findViewById(R.id.sourceCheck)
            sourceRegion=view.findViewById(R.id.sourceArea)
            sourceRegion.setOnClickListener(this)
        }

        fun initSource(sourceModel:ServerSourceModel){
            sourceTitle.text=sourceModel.name
            sourceDescription.text=sourceModel.description
            sourceCategory.text=String.format(context.getString(R.string.category_predicate),sourceModel.category)
            sourceLanguage.text=String.format(context.getString(R.string.language_predicate),sourceModel.language)
            sourceCheckBox.isChecked=sourceModel.isChecked
        }

        fun getList():List<ServerSourceModel>{
            return list
        }

        override fun onClick(p0: View?) {
            list[adapterPosition].isChecked=!list[adapterPosition].isChecked
            notifyItemChanged(adapterPosition)
            listener.onClick(p0)
        }
    }
}