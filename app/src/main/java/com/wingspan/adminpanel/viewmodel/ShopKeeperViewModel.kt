package com.wingspan.adminpanel.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wingspan.adminpanel.api.BaseUrlProvider

import com.wingspan.adminpanel.model.ApprovedMerchants
import com.wingspan.adminpanel.model.RejectedMerchants

import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.AwaitingMerchants
import com.wingspan.adminpanel.model.ResponseData

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

    private val _merchatPendingResponse= MutableLiveData<List<AwaitingMerchants>?>()
    val merchatPendingResponse: LiveData<List<AwaitingMerchants>?> get() =_merchatPendingResponse

    private val _merchatPendingError= MutableLiveData<String>()
    val merchatPendingError: LiveData<String> get() =_merchatPendingError

    private val _merchatRejectAwaitResponse= MutableLiveData<ResponseData?>()
    val merchatRejectAwaitResponse: LiveData<ResponseData?> get() =_merchatRejectAwaitResponse

    private val _merchatRejectAwaitError= MutableLiveData<String>()
    val merchatRejectAwaitError: LiveData<String> get() =_merchatRejectAwaitError

    private val _merchatApprovedAwaitResponse= MutableLiveData<ResponseData?>()
    val merchatApprovedAwaitResponse: LiveData<ResponseData?> get() =_merchatApprovedAwaitResponse

    private val _merchatApprovedAwaitError= MutableLiveData<String>()
    val merchatApprovedAwaitError: LiveData<String> get() =_merchatApprovedAwaitError

    private val _isLoading=MutableLiveData<Boolean>()
    val isLoading:LiveData<Boolean> get() =_isLoading

    fun approvedMerchatApi(isRefresh:Boolean){


            viewModelScope.launch {
                try{
                    if(!isRefresh){
                        _isLoading.value=true
                    }
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
                    if(!isRefresh){
                        _isLoading.value=false
                    }
                }
            }
        }



    fun rejectedMerchatApi(isRefresh:Boolean){


        viewModelScope.launch {
                try{
                    if(!isRefresh){
                        _isLoading.value=true
                    }
                    Log.d("rejectedMerchatApi","rejectedMerchatApi request--->")
                    val response= withContext(Dispatchers.IO){
                        BaseUrlProvider.create().rejectedMerchants()
                    }

                    Log.d("rejectedMerchatApi","rejectedMerchatApi response--->...${response.code()}...${response.body()}")
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
                    if(!isRefresh){
                        _isLoading.value=false
                    }
                }
            }
        }


    fun awaitingMerchatApi(isRefresh:Boolean){

        viewModelScope.launch {
                try{
                    if(!isRefresh){
                        _isLoading.value=true
                    }

                    Log.d("getFlashSale","getFlashSale request--->")
                    val response= withContext(Dispatchers.IO){
                        BaseUrlProvider.create().awaitingMerchants()
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
                    if(!isRefresh){
                        _isLoading.value=false
                    }
                }
            }
        }

    fun rejectShopkeeperApi(id:String,context: Context){
        val authHeaderValues= Extensions.getAuthHeaderValues(context)
        viewModelScope.launch {
            try{
               // _isLoading.value=true
                Log.d("rejectShopkeeperApi","rejectShopkeeperApi request--->$id")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().postRejectShopkeeper(authHeaderValues,id)
                }

                Log.d("rejectShopkeeperApi","rejectShopkeeperApi response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _merchatRejectAwaitResponse.postValue(responseData)


                        } else {
                            _merchatRejectAwaitError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _merchatRejectAwaitError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _merchatRejectAwaitError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){

                _merchatRejectAwaitError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                //_isLoading.value=false
            }
        }
    }

    fun acceptShopkeeperApi(id:String,context: Context){
        val authHeaderValues= Extensions.getAuthHeaderValues(context)
        viewModelScope.launch {
            try{
                // _isLoading.value=true
                Log.d("acceptShopkeeperApi","acceptShopkeeperApi request--->$id")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().postAcceptShopkeeper(authHeaderValues,id)
                }

                Log.d("acceptShopkeeperApi","acceptShopkeeperApi response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _merchatApprovedAwaitResponse.postValue(responseData)


                        } else {
                            _merchatApprovedAwaitError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _merchatApprovedAwaitError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _merchatApprovedAwaitError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){

                _merchatApprovedAwaitError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                //_isLoading.value=false
            }
        }
    }

}