package com.wingspan.adminpanel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wingspan.adminpanel.api.BaseUrlProvider
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.LoginRequest
import com.wingspan.adminpanel.model.LoginResponse



import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivityViewModel:ViewModel() {
    private val _isDataValid= MutableLiveData<Boolean>()
    val isDataValid: LiveData<Boolean> get() =_isDataValid

    private val _userNameError= MutableLiveData<String>()
    val userNameError: LiveData<String> get()=_userNameError

    private val _passwordError= MutableLiveData<String>()
    val passwordError: LiveData<String> get() =_passwordError

    private val _shopKeeperLoginError=MutableLiveData<String>()
    val shopKeeperLoginError:LiveData<String> get() =_shopKeeperLoginError

    private val _shopKeeperLoginResponseSuccess=MutableLiveData<LoginResponse?>()
    val shopKeeperLoginResponseSuccess:LiveData<LoginResponse?> get() =_shopKeeperLoginResponseSuccess

    private val _isLoading=MutableLiveData<Boolean>()
    val isLoading:LiveData<Boolean> get() =_isLoading

    fun validateInputs(userName: String, password: String) {
        _userNameError.value = if (userName.isEmpty()) "Email is required"  else null
        _passwordError.value = if (password.isEmpty()) "Password is required" else null
        _isDataValid.value=_userNameError.value == null && _passwordError.value == null
    }

    fun loginApiCall(userName:String,passwordText:String)
    {
        _isLoading.value=true
        viewModelScope.launch {
            try{
                val call= LoginRequest(userName,passwordText)
                Log.d("loginApiCall","loginApiCall request--->${call}")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().loginUser(call)
                }
                _isLoading.value=false
                Log.d("loginApiCall","loginApiCall response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        if (responseData != null) {
                            _shopKeeperLoginResponseSuccess.postValue(responseData)

                        } else {
                            _shopKeeperLoginError.postValue("Empty data")
                        }
                    }
                    400-> {
                        Extensions.handleErrorResponse(response.errorBody()){ errorMessage->
                            _shopKeeperLoginError.postValue(errorMessage)
                        }
                    }
                    401->{
                        Extensions.handleErrorResponse(response.errorBody()){errorMessage->
                            _shopKeeperLoginError.postValue(errorMessage)
                        }
                    }
                    403->{
                        Extensions.handleErrorResponse(response.errorBody()){errorMessage->
                            _shopKeeperLoginError.postValue(errorMessage)
                        }
                    }
                    404->{ Extensions.handleErrorResponse(response.errorBody()){errorMessage->
                        _shopKeeperLoginError.postValue(errorMessage)
                    }}
                    500->{  Extensions.handleErrorResponse(response.errorBody()){errorMessage->
                        _shopKeeperLoginError.postValue(errorMessage)
                    }}
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _shopKeeperLoginError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){
                _isLoading.value=false
                _shopKeeperLoginError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
        }

    }


}