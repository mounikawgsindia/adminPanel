package com.wingspan.adminpanel.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.model.ErrorResponse
import okhttp3.ResponseBody

import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


object Extensions {
    //(like maps, styles, and geolocation)


    fun View.setDebouncedClickListener(debounceTimeMillis:Long=500,onClick:()->Unit){
        val handler= Handler(Looper.getMainLooper())
        var lastClickTime=0L
        setOnClickListener {
            val currentTime=System.currentTimeMillis()
            if(currentTime-lastClickTime>=debounceTimeMillis){
                lastClickTime=currentTime
                handler.post(){
                    onClick()
                }
            }
        }
    }
    @SuppressLint("ObsoleteSdkInt")
    fun setStatusBarColor(activity: Activity?, colorResId: Int, isLightStatusBar: Boolean = true) {
        activity?.let {
            it.window?.statusBarColor = ContextCompat.getColor(it, colorResId)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Android 11 (API level 30) and above: use WindowInsetsController
                val windowInsetsController = it.window.insetsController
                windowInsetsController?.setSystemBarsAppearance(
                    if (isLightStatusBar) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                // Below Android 11: use systemUiVisibility flags
                @Suppress("DEPRECATION")
                it.window?.decorView?.systemUiVisibility = if (isLightStatusBar) {
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    0 // Clear the light status bar flag
                }
            }
        }
    }

    fun isNetworkAvailable(context: Context):Boolean
    {
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
    @SuppressLint("MissingInflatedId", "InflateParams")
    fun showNetworkAlertDialog(context: Context){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout=inflater.inflate(R.layout.alertdialog_network_connection,null)
        var dialog=AlertDialog.Builder(context).setView(layout).setCancelable(false).create()
        val close=layout.findViewById<AppCompatButton>(R.id.close)
        close.setOnClickListener(){
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.white_bg))
        dialog.show()

    }
    @SuppressLint("ClickableViewAccessibility")
    fun View.setupKeyboardHiding() {
        this.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val currentFocus = (this as? View)?.rootView?.findFocus()
                if (currentFocus is EditText) {
                    val outRect = Rect()
                    currentFocus.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        currentFocus.clearFocus()
                        hideKeyboard(currentFocus)
                    }
                }
            }
            false
        }
    }
    private fun hideKeyboard(view: View) {
        val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
    @SuppressLint("RestrictedApi")
    fun showCustomSnackbar(
        context: Context,
        message: String,color:Int
    ) {
        val activity = context as? Activity ?: return

        // Fetch the root view of the activity
        val rootView = activity.findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG)

        // Inflate custom view
        val customSnackbarView = LayoutInflater.from(context).inflate(R.layout.customised_snackbar, null)

        // Customize the layout elements inside customSnackbarView
        val errorMessage = customSnackbarView.findViewById<TextView>(R.id.snackbar_text)
        val viewColor = customSnackbarView.findViewById<View>(R.id.view)
        errorMessage.text = message
        Log.d("view color","view Color ${color}")
        val actualColor = ContextCompat.getColor(context, color)
        viewColor.setBackgroundColor(actualColor)
        // Add custom view to Snackbar
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0) // Remove default padding
        snackbarLayout.addView(customSnackbarView, 0)
        snackbarLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        snackbar.show()
    }
    fun handleErrorResponse(responseBody: ResponseBody?, errorCallback: (String) -> Unit) {
        responseBody?.let { errorBody ->
            val errorString = errorBody.string()
            val gson = Gson()
            try {
                val errorResponse = gson.fromJson(errorString, ErrorResponse::class.java)
                errorCallback(errorResponse.error)
            } catch (e: Exception) {
                Log.e("Error", "Parsing error response failed", e)
                errorCallback("Error parsing response")
            }
        }
    }
    fun handleErrorMessageResponse(responseBody: ResponseBody?, errorCallback: (String) -> Unit) {
        responseBody?.let { errorBody ->
            val errorString = errorBody.string()
            val gson = Gson()
            try {
                val errorResponse = gson.fromJson(errorString, ErrorResponse::class.java)
                errorCallback(errorResponse.message)
            } catch (e: Exception) {
                Log.e("Error", "Parsing error response failed", e)
                errorCallback("An unexpected error occurred")
            }
        }
    }
    fun togglePasswordVisibility(
        editText: EditText,
        event: MotionEvent,
        isPasswordVisible: Boolean
    ): Boolean {
        val drawableEnd = editText.compoundDrawablesRelative[2]
        if (drawableEnd != null && event.rawX >= (editText.right - drawableEnd.bounds.width())) {
            // Toggle password visibility
            val newVisibility = !isPasswordVisible
            if (newVisibility) {
                editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.hide, 0)
            } else {
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye, 0)
            }
            // Move the cursor to the end of the text
            editText.setSelection(editText.text.length)
            return true
        }
        return false
    }
    fun storeAuthCredentials(context: Context, apiKey: String, apiSecret: String) {
        // Create or get the master key
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            with(sharedPreferences.edit()) {
                putString("username", apiKey)
                putString("password", apiSecret)
                apply()
            }
        } catch (e: Exception) {
            // Log the exception
            Log.e("EncryptionError", "Error storing credentials", e)
        }
    }

    fun getAuthHeaderValues(context: Context): String {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            val userName = sharedPreferences.getString("username", null)
            val passwordAuth = sharedPreferences.getString("password", null)

            if (userName == null || passwordAuth == null) {
                return ""
            }

            val credentials = "$userName:$passwordAuth"
            "Basic " + java.util.Base64.getEncoder().encodeToString(credentials.toByteArray())
        } catch (e: Exception) {
            // Log the exception
            Log.e("DecryptionError", "Error retrieving credentials", e)
            ""
        }
    }
}