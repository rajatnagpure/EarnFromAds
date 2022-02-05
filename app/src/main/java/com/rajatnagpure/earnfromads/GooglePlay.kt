package com.rajatnagpure.earnfromads

import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class GooglePlay : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_play)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        toolbarTitle.text = resources.getString(R.string.google_play_recharge)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        val listView = findViewById<ListView>(R.id.list_view)
        val values = arrayOf( 40, 80, 100, 200, 500)
        val list = ArrayList<String>()
        for (element in values) {
            list.add("Rs. $element")
        }
        val adapter = GooglePlayListAdapter(this, list)
        listView.adapter = adapter


        listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val myEdit = sharedPreferences.edit()
            var amount = sharedPreferences.getFloat("amount",0.0f)
//            if(amount>values[position]){
//                //TODO get redeem code of value[position]
//                //TODO if successful less money from account

                val takingDetailsPopupWindows = TakingDetailsPopupWindows()
                takingDetailsPopupWindows.showPopupWindow(view)
//                amount -= values[position]
//                myEdit.putFloat("amount",amount)
//                myEdit.apply()
//            }else{
//                Toast.makeText(this, "Insufficient Amount!!", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item)
    }
}