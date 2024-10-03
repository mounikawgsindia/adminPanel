package com.wingspan.adminpanel.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.wingspan.adminpanel.MainActivity
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.databinding.ActivityLoginBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.extensions.Extensions.setupKeyboardHiding
import com.wingspan.adminpanel.utils.UserPreferences
import com.wingspan.adminpanel.viewmodel.LoginActivityViewModel


class LoginActivity : AppCompatActivity() {
    lateinit var _binding: ActivityLoginBinding
    val binding get()=_binding
    lateinit var userName:String
    lateinit var password:String
    lateinit var sharedPreferences: UserPreferences
    private var isPasswordVisible = false
    val viewModel: LoginActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Extensions.setStatusBarColor(this@LoginActivity, R.color.white)
        sharedPreferences= UserPreferences(this@LoginActivity)
        setUI()
        setObservers()
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setUI(){
        binding.apply {
            loginBtn.setDebouncedClickListener {
                userName=usernameEt.text.toString()
                password=passwordEt.text.toString()
                viewModel.validateInputs(userName,password)
            }
            register.setDebouncedClickListener(){
                startActivity(Intent(this@LoginActivity, RegistrationPage::class.java))
            }

            passwordEt.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    // Call the utility function to toggle password visibility
                    val isToggled =
                        Extensions.togglePasswordVisibility(passwordEt, event, isPasswordVisible)
                    if (isToggled) {
                        isPasswordVisible = !isPasswordVisible
                        return@setOnTouchListener true
                    } else {
                        // EditText itself clicked (excluding the drawableEnd)
                        passwordEt.performClick()
                        passwordEt.textSize = 16f // To trigger the default click behavior
                        return@setOnTouchListener false
                    }
                }
                false
            }
            root1.setupKeyboardHiding()
            loginBtn.setupKeyboardHiding()
        }
    }
    private fun setObservers(){
        binding.apply {
            viewModel.userNameError.observe(this@LoginActivity) { error ->
                usernameErrorTV.text=error
                if (error.isNullOrEmpty())
                {
                    usernameErrorTV.visibility = TextView.GONE
                } else
                {
                    usernameErrorTV.visibility = TextView.VISIBLE
                }
            }
            viewModel.passwordError.observe(this@LoginActivity) { error ->
                passwordErrorTV.text=error
                if (error.isNullOrEmpty())
                {
                    passwordErrorTV.visibility = TextView.GONE
                } else
                {
                    passwordErrorTV.visibility = TextView.VISIBLE
                }
            }

            viewModel.isLoading.observe(this@LoginActivity){isLoaing->
                if(isLoaing){
                    binding.loader.visibility=View.VISIBLE
                }else{
                    binding.loader.visibility=View.GONE
                }

            }
            viewModel.isDataValid.observe(this@LoginActivity)
            {isValid->
                if(isValid){
                    if(Extensions.isNetworkAvailable(this@LoginActivity))
                    {
                        viewModel.loginApiCall(userName,password)
                    }
                    else{
                        Extensions.showNetworkAlertDialog(this@LoginActivity)
                    }
                }


            }
            viewModel.shopKeeperLoginError.observe(this@LoginActivity) { error ->
                Log.d("shopKeeperRegistrationError","shopKeeperRegistrationError $error")
                Extensions.showCustomSnackbar(this@LoginActivity,error,R.color.light_red)
            }
            viewModel.shopKeeperLoginResponseSuccess.observe(this@LoginActivity){response->
               // Extensions.showCustomSnackbar(this@LoginActivity,response!!.message,ContextCompat.getColor(this@LoginActivity,R.color.green))
                sharedPreferences.saveData(response?.admin?.id!!,response.admin.username!!)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

    }
}