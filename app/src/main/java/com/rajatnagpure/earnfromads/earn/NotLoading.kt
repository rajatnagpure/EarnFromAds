package com.rajatnagpure.earnfromads.earn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.rajatnagpure.earnfromads.R

class NotLoading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_loading)
        val cross = findViewById<ImageView>(R.id.cross)
        cross.setOnClickListener{
            finish()
        }
    }
}