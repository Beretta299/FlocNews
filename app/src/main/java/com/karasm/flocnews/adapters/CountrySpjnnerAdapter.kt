package com.karasm.flocnews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.karasm.flocnews.R
import com.karasm.flocnews.models.CountryModel

class CountrySpjnnerAdapter(val context: Context, val list:List<CountryModel>):BaseAdapter(),SpinnerAdapter {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater=LayoutInflater.from(context)
        val view=layoutInflater.inflate(R.layout.country_city_item,p2,false)
        val tView:TextView=view.findViewById(R.id.dataContainer)
        tView.text=list[p0].countryName
        return view
    }


    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}