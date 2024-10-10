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
import com.wingspan.adminpanel.model.AwaitingFlashSale
import com.wingspan.adminpanel.model.AwaitingNewArrivals
import com.wingspan.adminpanel.model.RejectedFlashSale
import com.wingspan.adminpanel.model.ResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewArrivalsViewModel: ViewModel() {
    private val _flashSaleAprovedResponse= MutableLiveData<List<ApprovedFlashSale>?>()
    val flashSaleAprovedResponse: LiveData<List<ApprovedFlashSale>?> get() =_flashSaleAprovedResponse

    private val _flashSaleAprovedError= MutableLiveData<String>()
    val flashSaleAprovedError: LiveData<String> get() =_flashSaleAprovedError

    private val _flashSaleRejectedResponse= MutableLiveData<List<RejectedFlashSale>?>()
    val flashSaleRejectedResponse: LiveData<List<RejectedFlashSale>?> get() =_flashSaleRejectedResponse

    private val _flashSaleRejectedError= MutableLiveData<String>()
    val flashSaleRejectedError: LiveData<String> get() =_flashSaleRejectedError

    private val _newArrivalsPendingResponse= MutableLiveData<List<AwaitingNewArrivals>?>()
    val newArrivalsPendingResponse: LiveData<List<AwaitingNewArrivals>?> get() =_newArrivalsPendingResponse

    private val _newArrivalsPendingError= MutableLiveData<String>()
    val newArrivalsPendingError: LiveData<String> get() =_newArrivalsPendingError

    private val _newArrivalsRejectAwaitResponse= MutableLiveData<ResponseData?>()
    val newArrivalsRejectAwaitResponse: LiveData<ResponseData?> get() =_newArrivalsRejectAwaitResponse

    private val _newArrivalsRejectAwaitError= MutableLiveData<String>()
    val newArrivalsRejectAwaitError: LiveData<String> get() =_newArrivalsRejectAwaitError

    private val _newArrivalsApprovedAwaitResponse= MutableLiveData<ResponseData?>()
    val newArrivalsApprovedAwaitResponse: LiveData<ResponseData?> get() =_newArrivalsApprovedAwaitResponse

    private val _newArrivalsApprovedAwaitError= MutableLiveData<String>()
    val newArrivalsApprovedAwaitError: LiveData<String> get() =_newArrivalsApprovedAwaitError


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

                Log.d("approvedMerchatApi","approvedMerchatApi request--->")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().approvedFlashSale()
                }

                Log.d("approvedMerchatApi","approvedMerchatApi response--->...${response.code()}...${response.body()}")
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

                _flashSaleAprovedError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
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
                Log.d("rejectedMerchatApi","rejectedMerchatApi request--->")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().rejectedFlashSale()
                }

                Log.d("rejectedMerchatApi","rejectedMerchatApi response--->...${response.code()}...${response.body()}")
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

                _flashSaleRejectedError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                if(!isRefresh){
                    _isLoading.value=false
                }
            }
        }
    }


    fun awaitingNewArrivalApi(isRefresh: Boolean){

        viewModelScope.launch {
            try{
                if(!isRefresh){
                    _isLoading.value=true
                }
                Log.d("awaitingNewArrivalApi","awaitingNewArrivalApi request--->")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().awaitingNewArrivals()
                }

                Log.d("awaitingNewArrivalApi","awaitingNewArrivalApi response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _newArrivalsPendingResponse.postValue(responseData)

                            Log.d("getFlashSale", "Caching response data: $responseData")
                        } else {
                            _newArrivalsPendingError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _newArrivalsPendingError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _newArrivalsPendingError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){

                _newArrivalsPendingError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                if(!isRefresh){
                    _isLoading.value=false
                }
            }
        }
    }

    fun rejectNewArrivalsApi(id:String,context: Context){
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
                            _newArrivalsRejectAwaitResponse.postValue(responseData)


                        } else {
                            _newArrivalsRejectAwaitError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _newArrivalsRejectAwaitError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _newArrivalsRejectAwaitError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){

                _newArrivalsRejectAwaitError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                //_isLoading.value=false
            }
        }
    }

    fun acceptNewArrivalsApi(id:String,context: Context){
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
                            _newArrivalsApprovedAwaitResponse.postValue(responseData)


                        } else {
                            _newArrivalsApprovedAwaitError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _newArrivalsApprovedAwaitError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _newArrivalsApprovedAwaitError.postValue("Unknown error occurred")
                    }

                } }
            catch(e:Exception){

                _newArrivalsApprovedAwaitError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
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

                Log.d("acceptShopkeeperApi","acceptShopkeeperApi response--->...${response.code()}...${response.body()}...${response.errorBody()?.string()}")
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

                _flashSaleApprovedAllError.postValue("Failed to fetch data:NetWork Issue ${e.message}")
                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                //_isLoading.value=false
            }
        }
    }
}