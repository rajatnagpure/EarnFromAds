package com.rajatnagpure.earnfromads

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.rajatnagpure.earnfromads.banktransfer.BankTransferList
import com.rajatnagpure.earnfromads.earn.Earn
import com.rajatnagpure.earnfromads.googleplay.GooglePlayRedeemList

import com.rajatnagpure.earnfromads.shared.Presets

import android.media.MediaPlayer
import android.view.View
import android.view.Window
import android.view.WindowManager

import android.widget.Button


class MainActivity : AppCompatActivity() {
    var amount = 0.0f
    var confettiView: nl.dionsegijn.konfetti.xml.KonfettiView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        confettiView = findViewById(R.id.confetti_view)
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
            val googlePlayIntent = Intent(this, GooglePlayRedeemList::class.java)
            startActivity(googlePlayIntent)
        }

        val bankTransfer = findViewById<LinearLayout>(R.id.bank_transfer)
        bankTransfer.setOnClickListener{
            val bankTransferIntent = Intent(this, BankTransferList::class.java)
            startActivity(bankTransferIntent)
        }

//        val paypal = findViewById<LinearLayout>(R.id.paypal)
//        paypal.setOnClickListener{
//            Toast.makeText(this,
//                resources.getString(R.string.coming_soon),
//                Toast.LENGTH_SHORT)
//                .show()
//        }

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
//        if(!sharedPreferences.contains("firstTime")){
//            if (!sharedPreferences.getBoolean("firstTime",true)) return
//            showJoiningBonusDialogue()
//            val myEdit = sharedPreferences.edit()
//            myEdit.putBoolean("firstTime",false)
//            myEdit.apply()
//        }
    }

    override fun onRestart() {
        super.onRestart()
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        if(!sharedPreferences.contains("firstTime")){
            if (!sharedPreferences.getBoolean("firstTime",true)) return
            showJoiningBonusDialogue()
            val myEdit = sharedPreferences.edit()
            myEdit.putBoolean("firstTime",false)
            myEdit.apply()
        }
    }

    private fun fireConfetti(){
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.confetti_sound)
        mediaPlayer.setOnCompletionListener { mp -> // TODO Auto-generated method stub
            var mp = mp
            mp!!.reset()
            mp!!.release()
            mp = null
        }
        confettiView!!.start(Presets.festive())
        mediaPlayer.start()
    }

    private fun showJoiningBonusDialogue() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.bonus_dialogue)
        dialog.setCancelable(false)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        (dialog.findViewById<View>(R.id.claim_button) as Button).setOnClickListener(View.OnClickListener { v ->
            dialog.dismiss()
            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            amount = sharedPreferences!!.getFloat("amount", 0.0f)
            val myEdit = sharedPreferences!!.edit()
            amount += 5.0F
            myEdit!!.putFloat("amount", amount)
            myEdit.apply()
            findViewById<TextView>(R.id.text_amount).text = "%.2f".format(amount)
            fireConfetti()
        })
        dialog.show()
        dialog.window!!.attributes = lp
    }
}