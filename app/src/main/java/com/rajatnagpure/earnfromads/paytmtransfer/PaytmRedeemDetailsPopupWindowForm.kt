package com.rajatnagpure.earnfromads.paytmtransfer

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rajatnagpure.earnfromads.R
import com.rajatnagpure.earnfromads.shared.StaticData
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class PaytmRedeemDetailsPopupWindowForm {
    private var closeButton: ImageButton? = null
    private var congratulationsTextLinearLayout: LinearLayout? = null
    private var detailsFieldsListLinearLayout: LinearLayout? = null

    private var titleText: TextView? = null
    private var nameTextInputLayout: TextInputLayout? = null
    private var nameTextInputEditText: TextInputEditText? = null
    private var ageTextInputLayout: TextInputLayout? = null
    private var ageTextInputEditText: TextInputEditText? = null
    private var phoneTextInputLayout: TextInputLayout? = null
    private var phoneTextInputEditText: TextInputEditText? = null
    private var emailTextInputLayout: TextInputLayout? = null
    private var emailTextInputEditText: TextInputEditText? = null
    private var countryTextInputLayout: TextInputLayout? = null
    private var countryTextInputEditText: AutoCompleteTextView? = null
    private var submitButton: Button? = null

    private var redeemed = false

    fun showPopupWindow(view: View, amountToBeDebited: Float) {
        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.paytm_redeem_details_popup_window_form, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupView.elevation = 4F
        }
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        popupWindow.dimBehind()
        closeButton = popupView.findViewById(R.id.close_button)
        closeButton?.setOnClickListener{
            popupWindow.dismiss()
        }

        congratulationsTextLinearLayout = popupView.findViewById(R.id.congratulations_text_linear_layout)
        detailsFieldsListLinearLayout = popupView.findViewById(R.id.details_fields_list_linear_layout_group)
        congratulationsTextLinearLayout?.visibility = LinearLayout.GONE

        titleText = popupView.findViewById(R.id.title_text)
        nameTextInputLayout = popupView.findViewById(R.id.name_text_input)
        ageTextInputLayout = popupView.findViewById(R.id.age_text_input)
        phoneTextInputLayout = popupView.findViewById(R.id.phone_text_input)
        emailTextInputLayout = popupView.findViewById(R.id.email_text_input)
        countryTextInputLayout = popupView.findViewById(R.id.country_text_input)
        nameTextInputEditText = popupView.findViewById(R.id.name_edit_text)
        ageTextInputEditText = popupView.findViewById(R.id.age_edit_text)
        phoneTextInputEditText = popupView.findViewById(R.id.phone_edit_text)
        emailTextInputEditText = popupView.findViewById(R.id.email_edit_text)
        countryTextInputEditText = popupView.findViewById(R.id.country_edit_text)
        submitButton = popupView.findViewById(R.id.submit_button)

        val adapter = ArrayAdapter(view.context,android.R.layout.simple_list_item_1,
            StaticData.countryWithCode
        )
        countryTextInputEditText?.setAdapter(adapter)
        countryTextInputEditText?.threshold = 1

        val privacyPolicyAndTermsAndConditionsTextView = popupView.findViewById<TextView>(R.id.term_and_condition_and_privacy_policy_text_view)
        privacyPolicyAndTermsAndConditionsTextView?.isClickable = true
        privacyPolicyAndTermsAndConditionsTextView?.movementMethod = LinkMovementMethod.getInstance()
        val text = "<a href='https://docs.google.com/document/d/1gVECPIIaBdZb5VdQJRSEtanF2SZVHvGiRsTTV9_VZhA/edit?usp=sharing'> Term &amp; Condition and Privacy Policy. </a>"
        privacyPolicyAndTermsAndConditionsTextView?.text = Html.fromHtml(text)

        submitButton?.setOnClickListener{
            if(!redeemed){
                if(validateName() && validateAge() && validatePhone() && validateEmail() && validateCountry()){
                    if(!isNetworkAvailable(view.context)){
                        val alertDialog = AlertDialog.Builder(view.context).create()
                        alertDialog.setTitle("")
                        alertDialog.setMessage("Please Check Internet Connection")
                        alertDialog.setButton("Ok",
                            DialogInterface.OnClickListener { dialog, which ->
                                //dismiss the dialog
                                alertDialog.dismiss()
                            })
                        alertDialog.show()
                    }else{
                        sendDataToCloud(view.context)
                    }
                }
            }else{
                val sharedPreferences = view.context.getSharedPreferences("MySharedPref",
                    Context.MODE_PRIVATE
                )
                val myEdit = sharedPreferences.edit()
                var amount = sharedPreferences.getFloat("amount",0.0f)
                amount -= amountToBeDebited
                myEdit.putFloat("amount",amount)
                myEdit.apply()
                popupWindow.dismiss()
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }

    private fun validateEmail(): Boolean {
        phoneTextInputLayout?.isErrorEnabled = false
        if (!TextUtils.isEmpty(emailTextInputEditText?.text)) {
            if(!Patterns.EMAIL_ADDRESS.matcher(emailTextInputEditText?.text.toString()).matches()){
                emailTextInputLayout?.isErrorEnabled = true
                emailTextInputLayout?.error = "Invalid Email"
            }else{
                return true
            }
        }
        emailTextInputLayout?.isErrorEnabled = true
        emailTextInputLayout?.error = "Email is Must"
        return false
    }

    private fun validatePhone(): Boolean {
        ageTextInputLayout?.isErrorEnabled = false
        if (!TextUtils.isEmpty(phoneTextInputEditText?.text)) {
            return if(!phoneTextInputEditText?.text.toString().matches("""[0-9]*""".toRegex())){
                phoneTextInputLayout?.isErrorEnabled = true
                phoneTextInputLayout?.error = "Invalid Phone No."
                false
            }else if(phoneTextInputEditText?.text.toString().length < 10){
                phoneTextInputLayout?.isErrorEnabled = true
                phoneTextInputLayout?.error = "At Least 10 Digits"
                false
            }else{
                true
            }
        }
        phoneTextInputLayout?.isErrorEnabled = true
        phoneTextInputLayout?.error = "Phone is Must"
        return false
    }

    private fun validateName(): Boolean {
        if (!TextUtils.isEmpty(nameTextInputEditText?.text)) {
            if(nameTextInputEditText?.text.toString().length > 3){
                return true
            }else{
                nameTextInputLayout?.isErrorEnabled = true
                nameTextInputLayout?.error = "First & Last Name are Must"
            }
        }
        nameTextInputLayout?.isErrorEnabled = true
        nameTextInputLayout?.error = "First & Last Name are Must"
        return false
    }

    private fun validateAge(): Boolean {
        nameTextInputLayout?.isErrorEnabled = false
        if (!TextUtils.isEmpty(ageTextInputEditText?.text)) {
            return if((ageTextInputEditText?.text.toString().toInt()) < 4 || (ageTextInputEditText?.text.toString().toInt()) > 80){
                ageTextInputLayout?.isErrorEnabled = true
                ageTextInputLayout?.error = "Invalid Age"
                false
            }else{
                true
            }
        }
        ageTextInputLayout?.isErrorEnabled = true
        ageTextInputLayout?.error = "Age is Must"
        return false
    }

    private fun validateCountry(): Boolean {
        emailTextInputLayout?.isErrorEnabled = false
        if (!TextUtils.isEmpty(countryTextInputEditText?.text)) {
            if(countryTextInputEditText?.text.toString().length > 5){
                return true
            }else{
                countryTextInputLayout?.isErrorEnabled = true
                countryTextInputLayout?.error = "country is Must"
            }
        }
        countryTextInputLayout?.isErrorEnabled = true
        countryTextInputLayout?.error = "country is Must"
        return false
    }

    private fun PopupWindow.dimBehind() {
        val container = contentView.rootView
        val context = contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.3f
        wm.updateViewLayout(container, p)
    }

    private fun sendDataToCloud(context: Context){

        class SendPostReqAsyncTask : AsyncTask<String, String, String>() {
            private var loadingDialog: Dialog? = null
            override fun onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(context, "REDEEMING", "REDEEMING...");
            }

            override fun doInBackground(vararg urls: String?): String {
                var urlConnection: HttpURLConnection? = null
                val url = URL("https://script.google.com/macros/s/AKfycbwcZo1U3LeKkOduDDnvNXgwgOYs6Cf3UA1t6WjKgpnIlMHqQ6VyNfA98pNt0irIKDpMKg/exec?action=addGoogleRedeemCodeData")
                val jsonObject = JSONObject()
                jsonObject.put("name", nameTextInputEditText?.text.toString())
                jsonObject.put("age", ageTextInputEditText?.text.toString())
                jsonObject.put("mobile", phoneTextInputEditText?.text.toString())
                jsonObject.put("email", emailTextInputEditText?.text.toString())
                jsonObject.put("countryOrAddress", countryTextInputEditText?.text.toString())
                val jsonObjectString = jsonObject.toString()

                try {

                    urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "POST"
                    urlConnection.setRequestProperty("Content-Type", "application/json") // The format of the content we're sending to the server
                    urlConnection.doInput = true
                    urlConnection.doOutput = true

                    val outputStreamWriter = OutputStreamWriter(urlConnection.outputStream)
                    outputStreamWriter.write(jsonObjectString)
                    outputStreamWriter.flush()

                    val responseCode = urlConnection.responseCode
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        val `in` = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val sb = StringBuffer("")
                        var line: String? = ""
                        while (`in`.readLine().also { line = it } != null) {
                            sb.append(line)
                            break
                        }
                        `in`.close()
                        return sb.toString()
                    }
                } catch (ex: Exception) {

                } finally {
                    urlConnection?.disconnect()
                }

                return " "
            }

            override fun onPostExecute(result: String?) {
                loadingDialog?.dismiss()
                redeemed = !redeemed
                submitButton?.text = "Close"
                titleText?.text = "REDEEMED!"
                detailsFieldsListLinearLayout?.visibility = LinearLayout.GONE
                congratulationsTextLinearLayout?.visibility = LinearLayout.VISIBLE
            }
        }

        val sendPostReqAsyncTask = SendPostReqAsyncTask()
        sendPostReqAsyncTask.execute()
    }
}