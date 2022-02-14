package com.rajatnagpure.earnfromads.banktransfer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.rajatnagpure.earnfromads.R
import com.rajatnagpure.earnfromads.googleplay.GooglePlayRedeemDetailsPopupWindowsForm

class BankTransferList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_transfer_list)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        toolbarTitle.text = resources.getString(R.string.bank_transfer)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val listView = findViewById<ListView>(R.id.list_view)
        val values = arrayOf( 98, 198, 298, 399, 500)
        val list = ArrayList<String>()
        for (element in values) {
            list.add("Rs. $element")
        }
        val adapter = BankTransferListAdaptor(this, list)
        listView.adapter = adapter


        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                val amount = sharedPreferences.getFloat("amount", 0.0f)
                if (amount > values[position]) {
                    //TODO get redeem code of value[position]
                    //TODO if successful less money from account

                    val takingDetailsPopupWindows = BankTransferDetailsPopupWindowsForm()
                    takingDetailsPopupWindows.showPopupWindow(view, values[position].toFloat())
                } else {
                    Toast.makeText(this, "Insufficient Amount!!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item)
    }
}