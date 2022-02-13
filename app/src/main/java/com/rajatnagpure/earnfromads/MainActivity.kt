package com.rajatnagpure.earnfromads

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    var amount = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        toolbarTitle.text = toolbar.title;
        toolbar.setTitleTextColor(resources.getColor(R.color.grey_600))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val amountText = findViewById<TextView>(R.id.text_amount)
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        amount = sharedPreferences.getFloat("amount",0.0f)
        amountText.text = "%.2f".format(amount)

        val earnButton = findViewById<Button>(R.id.earn_button)
        earnButton.setOnClickListener {
            val earnIntent = Intent(this, Earn::class.java)
            startActivity(earnIntent)
        }

        val googlePlay = findViewById<LinearLayout>(R.id.google_play)
        googlePlay.setOnClickListener{
            val googlePlayIntent = Intent(this, GooglePlay::class.java)
            startActivity(googlePlayIntent)
        }

        val bankTransfer = findViewById<LinearLayout>(R.id.bank_transfer)
        bankTransfer.setOnClickListener{
            Toast.makeText(this,
                resources.getString(R.string.coming_soon),
                Toast.LENGTH_SHORT)
                .show()
        }

        val paypal = findViewById<LinearLayout>(R.id.paypal)
        paypal.setOnClickListener{
            Toast.makeText(this,
                resources.getString(R.string.coming_soon),
                Toast.LENGTH_SHORT)
                .show()
        }

        val paytm = findViewById<LinearLayout>(R.id.paytm)
        paytm.setOnClickListener{
            Toast.makeText(this,
                resources.getString(R.string.coming_soon),
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        val amountText = findViewById<TextView>(R.id.text_amount)
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        amount = sharedPreferences.getFloat("amount",0.0f)
        amountText.text = "%.2f".format(amount)
    }
}