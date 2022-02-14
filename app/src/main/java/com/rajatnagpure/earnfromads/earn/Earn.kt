package com.rajatnagpure.earnfromads.earn

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import com.unity3d.ads.IUnityAdsListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAds.FinishState
import com.unity3d.ads.UnityAds.UnityAdsError
import android.widget.*
import com.rajatnagpure.earnfromads.R


class Earn : AppCompatActivity() {
    private var amount = 0.0f
    private var enable: Boolean? = null
    private val unityGameID = "4161707"
    private val testMode = false
    private val surfacingId = "Rewarded_Android"
    private var watchUnityAd: Button? = null
    private var playQurekaQuiz: Button? = null
    private var playQuerkaLiteQuiz: Button? = null
    private var amountText: TextView? = null
    private var videoLoading: TextView? = null
    private var sharedPreferences: SharedPreferences? = null
    private var myEdit:SharedPreferences.Editor? = null

    private var anim:Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earn)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        toolbarTitle.text =resources.getString(R.string.earn);
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        videoLoading = findViewById<TextView>(R.id.video_ready)
        anim = AlphaAnimation(0.0f, 1.0f)
        anim?.duration = 750
        anim?.startOffset = 20
        anim?.repeatMode = Animation.REVERSE
        anim?.repeatCount = Animation.INFINITE
        videoLoading?.startAnimation(anim)
        videoLoading?.text = "VIDEO LOADING..."

        amountText = findViewById<TextView>(R.id.text_amount)
        watchUnityAd = findViewById<Button>(R.id.button_watch_unity_ad)
        playQuerkaLiteQuiz = findViewById(R.id.button_play_querka_quiz)
        playQuerkaLiteQuiz = findViewById<Button>(R.id.button_play_querka_quiz)


        // Unity Ads Part
        enable = false
        val myAdsListener = UnityAdsListener()
        UnityAds.addListener(myAdsListener)
        UnityAds.initialize(this, unityGameID, testMode)
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        amount = sharedPreferences!!.getFloat("amount", 0.0f)
        myEdit = sharedPreferences!!.edit()
        amountText?.text = "%.2f".format(amount)

        watchUnityAd?.setOnClickListener{
            displayRewardedAd()
        }

        playQuerkaLiteQuiz?.setOnClickListener{ view ->
            Toast.makeText(this, "Coming Soon...", Toast.LENGTH_LONG).show()
        }

        val notLoading = findViewById<CardView>(R.id.not_loading)
        notLoading.setOnClickListener{
            val notLoadingIntent = Intent(this, NotLoading::class.java)
            startActivity(notLoadingIntent);
        }
    }
    fun updateAmount(x: Float){
//        Toast.makeText(this, "Added amount $x", Toast.LENGTH_SHORT).show()
        amount += x
        myEdit!!.putFloat("amount", amount)
        myEdit!!.apply()
        amountText?.text = "%.2f".format(amount)
    }
    /////////////////////////////Qureka Lite Quiz Part//////////////////////////




    ///////////////////////////// Unity Ads Part ///////////////////////////////
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item)
    }
    private fun displayRewardedAd() {
        if (UnityAds.isReady(surfacingId)) {
            UnityAds.show(this, surfacingId)
        }
    }
    private inner class UnityAdsListener : IUnityAdsListener {
        override fun onUnityAdsReady(surfacingId: String) {
            videoLoading?.clearAnimation()
            videoLoading?.text = "VIDEO READY"
        }
        override fun onUnityAdsStart(surfacingId: String) {}
        override fun onUnityAdsFinish(surfacingId: String, finishState: FinishState) {
            if (finishState == FinishState.COMPLETED) {
                updateAmount(1.2f)
                Snackbar.make(
                    findViewById<RelativeLayout>(R.id.page_id),
                    "Money Added to A/C",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else if (finishState == FinishState.SKIPPED) {
                Snackbar.make(
                    findViewById<RelativeLayout>(R.id.page_id),
                    "Ad Skipped! No Money...",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else if (finishState == FinishState.ERROR) {
                Snackbar.make(
                    findViewById<RelativeLayout>(R.id.page_id),
                    "Error! while Playing Ad",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        override fun onUnityAdsError(error: UnityAdsError, message: String) {
            Log.e("Unity", "onUnityAdsError: $message")
        }
    }
}