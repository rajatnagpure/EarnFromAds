package com.rajatnagpure.earnfromads

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class GooglePlayListAdapter(context: Context, values: ArrayList<String>) : ArrayAdapter<String>(context, 0, values) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.google_play_list_item, parent, false)
        }
        val currentItem = getItem(position)
        val playAmount = listItemView!!.findViewById<TextView>( R.id.google_play_amount)
        playAmount.text = currentItem
        return listItemView
    }
}