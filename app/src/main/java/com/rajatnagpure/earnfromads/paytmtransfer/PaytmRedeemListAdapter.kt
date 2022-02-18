package com.rajatnagpure.earnfromads.paytmtransfer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.rajatnagpure.earnfromads.R

class PaytmRedeemListAdapter(context: Context, values: ArrayList<String>) : ArrayAdapter<String>(context, 0, values) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.paytm_redeem_list_item, parent, false)
        }
        val currentItem = getItem(position)
        val playAmount = listItemView!!.findViewById<TextView>(R.id.paytm_amount)
        playAmount.text = currentItem
        return listItemView
    }
}