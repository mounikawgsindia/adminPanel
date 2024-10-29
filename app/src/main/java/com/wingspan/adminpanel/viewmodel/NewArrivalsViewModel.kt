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
import com.wingspan.adminpanel.model.ApprovedNewArrivals
import com.wingspan.adminpanel.model.AwaitingFlashSale
import com.wingspan.adminpanel.model.AwaitingNewArrivals
import com.wingspan.adminpanel.model.RejectedFlashSale
import com.wingspan.adminpanel.model.RejectedNewArrivals
import com.wingspan.adminpanel.model.ResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class NewArrivalsViewModel: ViewModel() {
    private val _newArrivalsAprovedResponse= MutableLiveData<List<ApprovedNewArrivals>?>()
    val newArrivalsAprovedResponse: LiveData<List<ApprovedNewArrivals>?> get() =_newArrivalsAprovedResponse

    private val _newArrivalsAprovedError= MutableLiveData<String>()
    val newArrivalsAprovedError: LiveData<String> get() =_newArrivalsAprovedError

    private val _newArrivalsRejectedResponse= MutableLiveData<List<RejectedNewArrivals>?>()
    val newArrivalsRejectedResponse: LiveData<List<RejectedNewArrivals>?> get() =_newArrivalsRejectedResponse

    private val _newArrivalsRejectedError= MutableLiveData<String>()
    val newArrivalsRejectedError: LiveData<String> get() =_newArrivalsRejectedError

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


    private val _newArrivalsApprovedAllResponse= MutableLiveData<ResponseData?>()
    val newArrivalsApprovedAllResponse: LiveData<ResponseData?> get() =_newArrivalsApprovedAllResponse

    private val _newArrivalsApprovedAllError= MutableLiveData<String>()
    val newArrivalsApprovedAllError: LiveData<String> get() =_newArrivalsApprovedAllError
    private val _isLoading= MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() =_isLoading

    fun approvedNewArrivalsApi(isRefresh:Boolean){

        viewModelScope.launch {
            try{
                if(!isRefresh){
                    _isLoading.value=true
                }

                Log.d("approvedNewArrivalsApi","approvedNewArrivalsApi request--->")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().approvedNewArrivals()
                }

                Log.d("approvedNewArrivalsApi","approvedNewArrivalsApi response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _newArrivalsAprovedResponse.postValue(responseData)

                            Log.d("getFlashSale", "Caching response data: $responseData")
                        } else {
                            _newArrivalsAprovedError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _newArrivalsAprovedError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _newArrivalsAprovedError.postValue("Unknown error occurred")
                    }

                } }
            catch (e: IOException) {
                Log.e("NetworkError", "Network connection issue: ${e.message}")
                _newArrivalsAprovedError.postValue("Network connection issue, please try again later.")
            }
            catch(e:Exception){
                _newArrivalsAprovedError.postValue("Failed to fetch data: ${e.message}")
                Log.e("error", "Failed to fetch data: ${e.message}")
            }
            finally{
                if(!isRefresh){
                    _isLoading.value=false
                }
            }
        }
    }



    fun rejectedNewArrivalsApi(isRefresh :Boolean){


        viewModelScope.launch {
            try{
                if(!isRefresh){
                    _isLoading.value=true
                }
                Log.d("rejectedNewArrivalsApi","rejectedNewArrivalsApi request--->")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().rejectedNewArrivals()
                }

                Log.d("rejectedNewArrivalsApi","rejectedNewArrivalsApi response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _newArrivalsRejectedResponse.postValue(responseData)

                            Log.d("getFlashSale", "Caching response data: $responseData")
                        } else {
                            _newArrivalsRejectedError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _newArrivalsRejectedError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _newArrivalsRejectedError.postValue("Unknown error occurred")
                    }

                } }
            catch (e: IOException) {
                Log.e("NetworkError", "Network connection issue: ${e.message}")
                _newArrivalsRejectedError.postValue("Network connection issue, please try again later.")
            }
            catch(e:Exception){
                _newArrivalsRejectedError.postValue("Failed to fetch data: ${e.message}")
                Log.e("error", "Failed to fetch data: ${e.message}")
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
            catch (e: IOException) {
                Log.e("NetworkError", "Network connection issue: ${e.message}")
                _newArrivalsPendingError.postValue("Network connection issue, please try again later.")
            }
            catch(e:Exception){
                _newArrivalsPendingError.postValue("Failed to fetch data: ${e.message}")
                Log.e("error", "Failed to fetch data: ${e.message}")
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
                    BaseUrlProvider.create().getRejectNewArrivalsById(authHeaderValues,id)
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
            catch (e: IOException) {
                Log.e("NetworkError", "Network connection issue: ${e.message}")
                _newArrivalsRejectAwaitError.postValue("Network connection issue, please try again later.")
            }
            catch(e:Exception){
                _newArrivalsRejectAwaitError.postValue("Failed to fetch data: ${e.message}")
                Log.e("error", "Failed to fetch data: ${e.message}")
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
                    BaseUrlProvider.create().getAcceptNewArrivalsById(authHeaderValues,id)
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
            catch (e: IOException) {
                Log.e("NetworkError", "Network connection issue: ${e.message}")
                _newArrivalsApprovedAwaitError.postValue("Network connection issue, please try again later.")
            }
            catch(e:Exception){
                _newArrivalsApprovedAwaitError.postValue("Failed to fetch data: ${e.message}")
                Log.e("error", "Failed to fetch data: ${e.message}")
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
                    BaseUrlProvider.create().allApproveNewArrivals()
                }

                Log.d("acceptShopkeeperApi","acceptShopkeeperApi response--->...${response.code()}...${response.body()}...${response.errorBody()?.string()}")
                when (response.code()){
                    200->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _newArrivalsApprovedAllResponse.postValue(responseData)


                        } else {
                            _newArrivalsApprovedAllError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorMessageResponse(response.errorBody()) { errorMessage ->
                            _newArrivalsApprovedAllError.postValue("No pending or rejected flash sales to approve.")
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _newArrivalsApprovedAllError.postValue("Unknown error occurred")
                    }

                } }
            catch (e: IOException) {
                Log.e("NetworkError", "Network connection issue: ${e.message}")
                _newArrivalsApprovedAllError.postValue("Network connection issue, please try again later.")
            }
            catch(e:Exception){
                _newArrivalsApprovedAllError.postValue("Failed to fetch data: ${e.message}")
                Log.e("error", "Failed to fetch data: ${e.message}")
            }
            finally{
                //_isLoading.value=false
            }
        }
    }
}