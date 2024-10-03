package com.wingspan.adminpanel.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.activity.setViewTreeOnBackPressedDispatcherOwner
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.wingspan.adminpanel.R
import com.wingspan.adminpanel.databinding.ActivityRegistrationPageBinding
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.extensions.Extensions.setDebouncedClickListener
import com.wingspan.adminpanel.extensions.Extensions.setupKeyboardHiding
import com.wingspan.adminpanel.extensions.Extensions.togglePasswordVisibility
import com.wingspan.adminpanel.viewmodel.RegistrationPageViewModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import java.security.cert.Extension


class RegistrationPage : AppCompatActivity() {
    lateinit var _binding: ActivityRegistrationPageBinding
    val viewModel: RegistrationPageViewModel by viewModels()
    val binding get()=_binding
    private var isPasswordVisible = false
    lateinit var firstName:String
    lateinit var userName:String
    lateinit var email:String
    lateinit var passwordText:String
    lateinit var mobileNumber:String
    lateinit var lastName:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityRegistrationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Extensions.setStatusBarColor(this@RegistrationPage,R.color.white)
        setUI()
        setObservers()
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setUI(){
        binding.apply {
            creatAccount.setDebouncedClickListener() {
                firstName=firstnamET.text.toString()
                lastName=lastnameEt.text.toString()
                userName=usernameEt.text.toString()
                email=emailET.text.toString()
                passwordText=passwordEt.text.toString()
                mobileNumber=mobileNumberEt.text.toString()


                viewModel.isValidInputs(firstName,lastName,userName,email,passwordText,mobileNumber)
            }




            passwordEt.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    // Call the utility function to toggle password visibility
                    val isToggled = togglePasswordVisibility(passwordEt, event, isPasswordVisible)
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

            backIv.setDebouncedClickListener(){
                onBackPressedDispatcher.onBackPressed()
            }
            loginTv.setDebouncedClickListener(){
                val intent=Intent(this@RegistrationPage, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            root1.setupKeyboardHiding()
        }//end

    }
    private fun setObservers(){
        binding.apply {
            viewModel.firstNameError.observe(this@RegistrationPage)
            { error->
                firstNameErrorTV.text=error
                if (error.isNullOrEmpty())
                {
                    firstNameErrorTV.visibility = TextView.GONE
                } else
                {
                    firstNameErrorTV.visibility = TextView.VISIBLE
                }
            }
            viewModel.lastNameError.observe(this@RegistrationPage)
            { error->
                lastnameErrorTV.text=error
                if (error.isNullOrEmpty())
                {
                    lastnameErrorTV.visibility = TextView.GONE
                } else
                {
                    lastnameErrorTV.visibility = TextView.VISIBLE
                }
            }
            viewModel.userNameError.observe(this@RegistrationPage)
            { error->
                usernameErrorTV.text=error
                if (error.isNullOrEmpty()) {
                    usernameErrorTV.visibility = TextView.GONE
                } else {
                    usernameErrorTV.visibility = TextView.VISIBLE
                }
            }
            viewModel.emailError.observe(this@RegistrationPage)
            { error->
                emailErrorTV.text=error
                if (error.isNullOrEmpty())
                {
                    emailErrorTV.visibility = TextView.GONE
                } else {
                    emailErrorTV.visibility = TextView.VISIBLE
                }
            }
            viewModel.modeleNumberError.observe(this@RegistrationPage)
            { error->
                mobileNumberErrorTV.text=error
                if (error.isNullOrEmpty())
                {
                    mobileNumberErrorTV.visibility = TextView.GONE
                } else {
                    mobileNumberErrorTV.visibility = TextView.VISIBLE
                }
            }


            viewModel.passwordError.observe(this@RegistrationPage)
            { error->
                passwordErrorTV.text=error
                if(error.isNullOrEmpty()){
                    passwordErrorTV.visibility= View.GONE }
                else{ passwordErrorTV.visibility= View.VISIBLE }

            }

            viewModel.isDataValid.observe(this@RegistrationPage){isValid->
                Log.d("isDataValid isValid"," $isValid")
                if(isValid){
                    if(Extensions.isNetworkAvailable(this@RegistrationPage))
                    {
                        loader.visibility=View.VISIBLE
                        viewModel.registrationApiCall(firstName,lastName,userName,email,passwordText,mobileNumber)

                    }
                    else{
                        Extensions.showNetworkAlertDialog(this@RegistrationPage)
                    }
                }


            }
            viewModel.adminRegistrationResponse.observe(this@RegistrationPage){response->
                loader.visibility=View.GONE
                Extensions.showCustomSnackbar(this@RegistrationPage,response?.message.toString(),R.color.green)
            }
            viewModel.adminRegistrationError.observe(this@RegistrationPage){error->
                loader.visibility=View.GONE
                Log.d("shopKeeperRegistrationError","shopKeeperRegistrationError $error")
                Extensions.showCustomSnackbar(this@RegistrationPage,error,R.color.light_red)

            }

        }
    }

}