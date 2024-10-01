package com.wingspan.adminpanel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wingspan.adminpanel.api.BaseUrlProvider

import com.wingspan.adminpanel.model.ApprovedMerchants
import com.wingspan.adminpanel.model.PendingMerchants
import com.wingspan.adminpanel.model.RejectedMerchants

import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.shopkeeper.api.ApiDataCache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShopKeeperViewModel:ViewModel() {
    private val _merchatAprovedResponse= MutableLiveData<List<ApprovedMerchants>?>()
    val merchatAprovedResponse: LiveData<List<ApprovedMerchants>?> get() =_merchatAprovedResponse

    private val _merchatAprovedError= MutableLiveData<String>()
    val merchatAprovedError: LiveData<String> get() =_merchatAprovedError

    private val _merchatRejectedResponse= MutableLiveData<List<RejectedMerchants>?>()
    val merchatRejectedResponse: LiveData<List<RejectedMerchants>?> get() =_merchatRejectedResponse

    private val _merchatRejectedError= MutableLiveData<String>()
    val merchatRejectedError: LiveData<String> get() =_merchatRejectedError

    private val _merchatPendingResponse= MutableLiveData<List<PendingMerchants>?>()
    val merchatPendingResponse: LiveData<List<PendingMerchants>?> get() =_merchatPendingResponse

    private val _merchatPendingError= MutableLiveData<String>()
    val merchatPendingError: LiveData<String> get() =_merchatPendingError

    private val _isLoading=MutableLiveData<Boolean>()
    val isLoading:LiveData<Boolean> get() =_isLoading

    fun approvedMerchatApi(){


            viewModelScope.launch {
                try{
                    _isLoading.value=true
                    Log.d("approvedMerchatApi","approvedMerchatApi request--->")
                    val response= withContext(Dispatchers.IO){
                        BaseUrlProvider.create().approvedMerchants()
                    }

                    Log.d("approvedMerchatApi","approvedMerchatApi response--->...${response.code()}...${response.body()}")
                    when (response.code()){
                        200->{
                            val responseData = response.body()
                            // Cache the response

                            if (responseData != null) {
                                _merchatAprovedResponse.postValue(responseData)

                                Log.d("getFlashSale", "Caching response data: $responseData")
                            } else {
                                _merchatAprovedError.postValue("Empty data")
                            }
                        }
                        in 400..500 -> {
                            Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                                _merchatAprovedError.postValue(errorMessage)
                            }
                        }
                        else -> {
                            Log.d("response","--->error${response.code()}")
                            _merchatAprovedError.postValue("Unknown error occurred")
                        }

                    } }
                catch(e:Exception){

                    _merchatAprovedError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
                    Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
                }
                finally{
                    _isLoading.value=false
                }
            }
        }



    fun rejectedMerchatApi(){


        viewModelScope.launch {
                try{
                    _isLoading.value=true
                    Log.d("getFlashSale","getFlashSale request--->")
                    val response= withContext(Dispatchers.IO){
                        BaseUrlProvider.create().rejectedMerchants()
                    }

                    Log.d("getFlashSale","getFlashSale response--->...${response.code()}...${response.body()}")
                    when (response.code()){
                        200->{
                            val responseData = response.body()
                            // Cache the response

                            if (responseData != null) {
                                _merchatRejectedResponse.postValue(responseData)

                                Log.d("getFlashSale", "Caching response data: $responseData")
                            } else {
                                _merchatRejectedError.postValue("Empty data")
                            }
                        }
                        in 400..500 -> {
                            Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                                _merchatRejectedError.postValue(errorMessage)
                            }
                        }
                        else -> {
                            Log.d("response","--->error${response.code()}")
                            _merchatRejectedError.postValue("Unknown error occurred")
                        }

                    } }
                catch(e:Exception){

                    _merchatRejectedError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
                    Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
                }
                finally{
                    _isLoading.value=false
                }
            }
        }


    fun pendingMerchatApi(){

        viewModelScope.launch {
                try{
                    _isLoading.value=true
                    Log.d("getFlashSale","getFlashSale request--->")
                    val response= withContext(Dispatchers.IO){
                        BaseUrlProvider.create().pendingMerchants()
                    }

                    Log.d("getFlashSale","getFlashSale response--->...${response.code()}...${response.body()}")
                    when (response.code()){
                        200->{
                            val responseData = response.body()
                            // Cache the response

                            if (responseData != null) {
                                _merchatPendingResponse.postValue(responseData)

                                Log.d("getFlashSale", "Caching response data: $responseData")
                            } else {
                                _merchatPendingError.postValue("Empty data")
                            }
                        }
                        in 400..500 -> {
                            Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                                _merchatPendingError.postValue(errorMessage)
                            }
                        }
                        else -> {
                            Log.d("response","--->error${response.code()}")
                            _merchatPendingError.postValue("Unknown error occurred")
                        }

                    } }
                catch(e:Exception){

                    _merchatPendingError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
                    Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
                }
                finally{
                    _isLoading.value=false
                }
            }
        }



}