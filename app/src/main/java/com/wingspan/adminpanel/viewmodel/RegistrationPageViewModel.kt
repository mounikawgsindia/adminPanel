package com.wingspan.adminpanel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wingspan.adminpanel.api.BaseUrlProvider
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.RegistrationRequest
import com.wingspan.adminpanel.model.ResponseData

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegistrationPageViewModel: ViewModel() {
    lateinit var tripleRepeated:String
    private val _isDataValid = MutableLiveData<Boolean>()
    val isDataValid: LiveData<Boolean> get() = _isDataValid

    private val _firstNameError = MutableLiveData<String>()
    val firstNameError: LiveData<String> get() = _firstNameError

    private val _userNameError = MutableLiveData<String>()
    val userNameError: LiveData<String> get() = _userNameError

    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String> get() = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String> get() = _passwordError

    private val _modeleNumberError = MutableLiveData<String>()
    val modeleNumberError: LiveData<String> get() = _modeleNumberError

    private val _lastNameError = MutableLiveData<String>()
    val lastNameError: LiveData<String> get() = _lastNameError





    //api data
    private val _adminRegistrationError = MutableLiveData<String>()
    val adminRegistrationError: LiveData<String> get() = _adminRegistrationError

    private val _adminRegistrationResponse = MutableLiveData<ResponseData?>()
    val adminRegistrationResponse: LiveData<ResponseData?> get() = _adminRegistrationResponse

    fun isValidInputs(firstName: String, lastName: String,userName: String, email: String, password: String, modileNumber: String)
    {

        _firstNameError.value = if (firstName.isEmpty()) "Full Name required" else if (!isValidFirstName(firstName)) tripleRepeated else null
        _lastNameError.value = if (lastName.isEmpty()) "Full Name required" else if (!isValidFirstName(lastName)) tripleRepeated else null
        _userNameError.value = if (userName.isEmpty()) "User Name required" else if (!isValidFirstName(userName)) tripleRepeated else null
        _emailError.value = if (email.isEmpty()) "Email is required" else if (!isValidEmail(email)) "Enter valid email" else null
        _modeleNumberError.value = if (modileNumber.isEmpty()) "Mobilenumber is required" else if (!isValisNumber(modileNumber)) "MobileNumber must be 10 characters" else null
        _passwordError.value = if (password.isEmpty()) "Password is required" else if (!isValidPassword(password)) "Must contain atleast 8 characters and should have atleast one special character, one capital, one small and one numeric character." else null

        _isDataValid.value = _firstNameError.value == null && _userNameError.value == null && _emailError.value == null && _modeleNumberError.value == null &&
                    _passwordError.value == null && _lastNameError.value == null


    }
    fun registrationApiCall(firstName: String, lastName:String,userName:String,email:String,passwordText:String,mobileNumber:String)
    {

        viewModelScope.launch {
            try{
            val call= RegistrationRequest(firstName,lastName,userName,mobileNumber,email,passwordText)
            Log.d("registrationApiCall","registrationApiCall request--->${call}")
            val response= withContext(Dispatchers.IO){
                BaseUrlProvider.create().registerUser(call)
            }
            Log.d("registrationApiCall","registrationApiCall response--->...${response.code()}...${response.body()}")
                when (response.code()){
                in 200..201->{
                    val responseData = response.body()
                    if (responseData != null) {
                        _adminRegistrationResponse.postValue(responseData)
                    } else {
                        _adminRegistrationError.postValue("Empty data")
                    }
                    }
                in 400 ..500-> {
                    Extensions.handleErrorMessageResponse(response.errorBody()){ errorMessage->
                        _adminRegistrationError.postValue(errorMessage)
                    }
                }

                else -> {
                    Log.d("response","--->error1 ${response.code()}")
                    _adminRegistrationError.postValue("Unknown error occurred")
                }

            } }
            catch(e:Exception){
                _adminRegistrationError.postValue("Please check your Network connection")
                Log.e("error", "Failed to fetch data:Network issue ${e.message}")
            }
        }

    }
    private fun isValidFirstName(fName:String):Boolean{
        val repeatedPatternRegex = Regex("(.)\\1{2,}")

        return if (repeatedPatternRegex.containsMatchIn(fName)) {
            tripleRepeated="Name cannot contain repeated letters more than 2 times continuously"
            false
        } else{
            true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValisNumber(number:String):Boolean{
        return number.length==10
    }

    private fun isValidPassword(password:String):Boolean{
        val minLength = 8
        val uppercaseRegex = Regex("[A-Z]")
        val lowercaseRegex = Regex("[a-z]")
        val digitRegex = Regex("\\d")
        val specialCharRegex = Regex("[^A-Za-z0-9]")

        val isLengthValid = password.length >= minLength
        val isUppercaseValid = uppercaseRegex.containsMatchIn(password)
        val isLowercaseValid = lowercaseRegex.containsMatchIn(password)
        val isDigitValid = digitRegex.containsMatchIn(password)
        val isSpecialCharValid = specialCharRegex.containsMatchIn(password)
        return !(!isLengthValid || !isUppercaseValid || !isLowercaseValid || !isDigitValid || !isSpecialCharValid)
    }
}