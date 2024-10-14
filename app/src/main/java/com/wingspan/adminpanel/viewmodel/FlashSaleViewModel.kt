package com.wingspan.adminpanel.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wingspan.adminpanel.api.BaseUrlProvider
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.ApprovedFlashSale
import com.wingspan.adminpanel.model.ApprovedMerchants
import com.wingspan.adminpanel.model.AwaitingFlashSale
import com.wingspan.adminpanel.model.AwaitingMerchants
import com.wingspan.adminpanel.model.RejectedFlashSale
import com.wingspan.adminpanel.model.RejectedMerchants
import com.wingspan.adminpanel.model.ResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlashSaleViewModel:ViewModel() {
    private val _flashSaleAprovedResponse= MutableLiveData<List<ApprovedFlashSale>?>()
    val flashSaleAprovedResponse: LiveData<List<ApprovedFlashSale>?> get() =_flashSaleAprovedResponse

    private val _flashSaleAprovedError= MutableLiveData<String>()
    val flashSaleAprovedError: LiveData<String> get() =_flashSaleAprovedError

    private val _flashSaleRejectedResponse= MutableLiveData<List<RejectedFlashSale>?>()
    val flashSaleRejectedResponse: LiveData<List<RejectedFlashSale>?> get() =_flashSaleRejectedResponse

    private val _flashSaleRejectedError= MutableLiveData<String>()
    val flashSaleRejectedError: LiveData<String> get() =_flashSaleRejectedError

    private val _flashSalePendingResponse= MutableLiveData<List<AwaitingFlashSale>?>()
    val flashSalePendingResponse: LiveData<List<AwaitingFlashSale>?> get() =_flashSalePendingResponse

    private val _flashSalePendingError= MutableLiveData<String>()
    val flashSalePendingError: LiveData<String> get() =_flashSalePendingError

    private val _flashSaleRejectAwaitResponse= MutableLiveData<ResponseData?>()
    val flashSaleRejectAwaitResponse: LiveData<ResponseData?> get() =_flashSaleRejectAwaitResponse

    private val _flashSaleRejectAwaitError= MutableLiveData<String>()
    val flashSaleRejectAwaitError: LiveData<String> get() =_flashSaleRejectAwaitError

    private val _flashSaleApprovedAwaitResponse= MutableLiveData<ResponseData?>()
    val flashSaleApprovedAwaitResponse: LiveData<ResponseData?> get() =_flashSaleApprovedAwaitResponse

    private val _flashSaleApprovedAwaitError= MutableLiveData<String>()
    val flashSaleApprovedAwaitError: LiveData<String> get() =_flashSaleApprovedAwaitError


    private val _flashSaleApprovedAllResponse= MutableLiveData<ResponseData?>()
    val flashSaleApprovedAllResponse: LiveData<ResponseData?> get() =_flashSaleApprovedAllResponse

    private val _flashSaleApprovedAllError= MutableLiveData<String>()
    val flashSaleApprovedAllError: LiveData<String> get() =_flashSaleApprovedAllError
    private val _isLoading= MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() =_isLoading

    fun approvedFlashSaleApi(isRefresh:Boolean){

        viewModelScope.launch {
            try{
                if(!isRefresh){
                    _isLoading.value=true
                }

                Log.d("approvedFlashSaleApi","approvedFlashSaleApi request--->")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().approvedFlashSale()
                }

                Log.d("approvedFlashSaleApi ","approvedMerchatApi response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _flashSaleAprovedResponse.postValue(responseData)

                            Log.d("getFlashSale", "Caching response data: $responseData")
                        } else {
                            _flashSaleAprovedError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _flashSaleAprovedError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _flashSaleAprovedError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){

                _flashSaleAprovedError.postValue("Please check your Network connection")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                if(!isRefresh){
                    _isLoading.value=false
                }
            }
        }
    }



    fun rejectedFlashSaleApi(isRefresh :Boolean){


        viewModelScope.launch {
            try{
                if(!isRefresh){
                    _isLoading.value=true
                }
                Log.d("rejectedFlashSaleApi","rejectedFlashSaleApi request--->")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().rejectedFlashSale()
                }

                Log.d("rejectedFlashSaleApi","rejectedFlashSaleApi response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _flashSaleRejectedResponse.postValue(responseData)

                            Log.d("getFlashSale", "Caching response data: $responseData")
                        } else {
                            _flashSaleRejectedError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _flashSaleRejectedError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _flashSaleRejectedError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){

                _flashSaleRejectedError.postValue("Please check your Network connection")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                if(!isRefresh){
                    _isLoading.value=false
                }
            }
        }
    }


    fun awaitingFlashSaleApi(isRefresh: Boolean){

        viewModelScope.launch {
            try{
                if(!isRefresh){
                    _isLoading.value=true
                }
                Log.d("getFlashSale","getFlashSale request--->")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().awaitingFlashSale()
                }

                Log.d("getFlashSale","getFlashSale response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _flashSalePendingResponse.postValue(responseData)

                            Log.d("getFlashSale", "Caching response data: $responseData")
                        } else {
                            _flashSalePendingError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _flashSalePendingError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _flashSalePendingError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){

                _flashSalePendingError.postValue("Please check your Network connection")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                if(!isRefresh){
                    _isLoading.value=false
                }
            }
        }
    }

    fun rejectFlashSaleApi(id:String,context: Context){
        val authHeaderValues= Extensions.getAuthHeaderValues(context)
        viewModelScope.launch {
            try{
                // _isLoading.value=true
                Log.d("rejectShopkeeperApi","rejectShopkeeperApi request--->$id")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().getRejectFlashSaleById(authHeaderValues,id)
                }

                Log.d("rejectShopkeeperApi","rejectShopkeeperApi response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _flashSaleRejectAwaitResponse.postValue(responseData)


                        } else {
                            _flashSaleRejectAwaitError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _flashSaleRejectAwaitError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _flashSaleRejectAwaitError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){

                _flashSaleRejectAwaitError.postValue("Please check your Network connection")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                //_isLoading.value=false
            }
        }
    }

    fun acceptFlashSaleApi(id:String,context: Context){
        val authHeaderValues= Extensions.getAuthHeaderValues(context)
        viewModelScope.launch {
            try{
                // _isLoading.value=true
                Log.d("acceptShopkeeperApi","acceptShopkeeperApi request--->$id")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().getAcceptFlashSaleById(authHeaderValues,id)
                }

                Log.d("acceptShopkeeperApi","acceptShopkeeperApi response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _flashSaleApprovedAwaitResponse.postValue(responseData)


                        } else {
                            _flashSaleApprovedAwaitError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _flashSaleApprovedAwaitError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _flashSaleApprovedAwaitError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){

                _flashSaleApprovedAwaitError.postValue("Please check your Network connection")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                //_isLoading.value=false
            }
        }
    }
    fun approveallApi(){
        viewModelScope.launch {
            try{
                // _isLoading.value=true
                Log.d("approveallApi","approveallApi request--->")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().allApproveFlashSale()
                }

                Log.d("approveallApi","approveallApi response--->...${response.code()}...${response.body()}...${response.errorBody()?.string()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _flashSaleApprovedAllResponse.postValue(responseData)


                        } else {
                            _flashSaleApprovedAllError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorMessageResponse(response.errorBody()) { errorMessage ->
                            _flashSaleApprovedAllError.postValue("No pending or rejected flash sales to approve.")
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _flashSaleApprovedAllError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){

                _flashSaleApprovedAllError.postValue("Please check your Network connection")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                //_isLoading.value=false
            }
        }
    }
}