package com.rajatnagpure.earnfromads
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.view.MotionEvent
import android.view.View.OnTouchListener

class TakingDetailsPopupWindows {
    private var closeButton: ImageButton? = null
    private var congratulationsTextLinearLayout: LinearLayout? = null
    private var detailsFieldsListLinearLayout: LinearLayout? = null

    private var titleText: TextView? = null
    private var nameTextInputLayout: TextInputLayout? = null
    private var nameTextInputEditText: TextInputEditText? = null
    private var phoneTextInputLayout: TextInputLayout? = null
    private var phoneTextInputEditText: TextInputEditText? = null
    private var emailTextInputLayout: TextInputLayout? = null
    private var emailTextInputEditText: TextInputEditText? = null
    private var addressTextInputLayout: TextInputLayout? = null
    private var addressTextInputEditText: TextInputEditText? = null
    private var submitButton: Button? = null

    private var redeemed = false

    fun showPopupWindow(view: View) {
        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.taking_details_popup_window, null)
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
        detailsFieldsListLinearLayout = popupView.findViewById(R.id.details_fields_list_linear_layout)
        congratulationsTextLinearLayout?.visibility = LinearLayout.GONE

        titleText = popupView.findViewById(R.id.title_text)
        nameTextInputLayout = popupView.findViewById(R.id.name_text_input)
        phoneTextInputLayout = popupView.findViewById(R.id.phone_text_input)
        emailTextInputLayout = popupView.findViewById(R.id.email_text_input)
        addressTextInputLayout = popupView.findViewById(R.id.address_text_input)
        nameTextInputEditText = popupView.findViewById(R.id.name_edit_text)
        phoneTextInputEditText = popupView.findViewById(R.id.phone_edit_text)
        emailTextInputEditText = popupView.findViewById(R.id.email_edit_text)
        addressTextInputEditText = popupView.findViewById(R.id.address_edit_text)
        submitButton = popupView.findViewById(R.id.submit_button)

        submitButton?.setOnClickListener{
            if(!redeemed){
                if(validateName() && validatePhone() && validateEmail() && validateAddress()){
                    redeemed = !redeemed
                    submitButton?.text = "Close"
                    titleText?.text = "REDEEMED!"
                    detailsFieldsListLinearLayout?.visibility = LinearLayout.GONE
                    congratulationsTextLinearLayout?.visibility = LinearLayout.VISIBLE
                }
            }else{
                popupWindow.dismiss()
            }
        }
    }

    private fun validateEmail(): Boolean {
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
        if (!TextUtils.isEmpty(phoneTextInputEditText?.text)) {
            if(!Patterns.PHONE.matcher(phoneTextInputEditText?.text.toString()).matches()){
                phoneTextInputLayout?.isErrorEnabled = true
                phoneTextInputLayout?.error = "Invalid Phone No."
            }else{
                return true
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
                nameTextInputLayout?.error = "Name is Must"
            }
        }
        nameTextInputLayout?.isErrorEnabled = true
        nameTextInputLayout?.error = "Name is Must"
        return false
    }

    private fun validateAddress(): Boolean {
        if (!TextUtils.isEmpty(addressTextInputEditText?.text)) {
            if(addressTextInputEditText?.text.toString().length > 5){
                return true
            }else{
                addressTextInputLayout?.isErrorEnabled = true
                addressTextInputLayout?.error = "Address is Must"
            }
        }
        addressTextInputLayout?.isErrorEnabled = true
        addressTextInputLayout?.error = "Address is Must"
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
}