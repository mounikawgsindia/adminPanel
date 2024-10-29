package com.wingspan.adminpanel.viewmodel

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wingspan.adminpanel.api.BaseUrlProvider
import com.wingspan.adminpanel.extensions.Extensions
import com.wingspan.adminpanel.model.CategoriesModel
import com.wingspan.adminpanel.model.CategoryPostRequest
import com.wingspan.adminpanel.model.ResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

class CategoriesViewModel: ViewModel() {


    private val _categoryResponse= MutableLiveData<List<CategoriesModel>?>()
    val categoryResponse: LiveData<List<CategoriesModel>?> get() =_categoryResponse

    private val _categoryResponseError= MutableLiveData<String>()
    val categoryResponseError: LiveData<String> get() =_categoryResponseError

    private val _categoryPostResponse= MutableLiveData<ResponseData?>()
    val categoryPostResponse: LiveData<ResponseData?> get() =_categoryPostResponse

    private val _categoryPostResponseError= MutableLiveData<String>()
    val categoryPostResponseError: LiveData<String> get() =_categoryPostResponseError

    private val _categoryUpdateResponse= MutableLiveData<ResponseData?>()
    val categoryUpdateResponse: LiveData<ResponseData?> get() =_categoryUpdateResponse

    private val _categoryUpdateResponseError= MutableLiveData<String>()
    val categoryUpdateResponseError: LiveData<String> get() =_categoryUpdateResponseError

    private val _isLoading= MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() =_isLoading

    private val _categoryDeleteResponse= MutableLiveData<ResponseData>()
    val categoryDeleteResponse: LiveData<ResponseData> get() =_categoryDeleteResponse

    private val _categoryDeleteError= MutableLiveData<String>()
    val categoryDeleteError: LiveData<String> get() =_categoryDeleteError


    fun getCategories(isRefreshPage:Boolean){


            _isLoading.value=true
            viewModelScope.launch {
                try{

                    Log.d("getCategories","getCategories request--->")
                    val response= withContext(Dispatchers.IO){
                        BaseUrlProvider.create().getCategories()
                    }

                    Log.d("getCategories","getCategories response--->...${response.code()}...${response.body()}")
                    when (response.code()){
                        in 200..201->{
                            val responseData = response.body()
                            // Cache the response

                            if (responseData != null) {
                                _categoryResponse.postValue(responseData)

                                Log.d("getFlashSale", "Caching response data: $responseData")
                            } else {
                                _categoryResponseError.postValue("Empty data")
                            }
                        }
                        in 400..500 -> {
                            Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                                _categoryResponseError.postValue(errorMessage)
                            }
                        }
                        else -> {
                            Log.d("response","--->error${response.code()}")
                            _categoryResponseError.postValue("Unknown error occurred")
                        }

                    } }
                catch (e: IOException) {
                    Log.e("NetworkError", "Network connection issue: ${e.message}")
                    _categoryResponseError.postValue("Network connection issue, please try again later.")
                }
                catch(e:Exception){
                    _categoryResponseError.postValue("Failed to fetch data: ${e.message}")
                    Log.e("error", "Failed to fetch data: ${e.message}")
                }
                finally{
                    _isLoading.value=false
                }
            }
        }
    fun uploadCategory(categoryName:String){


        _isLoading.value=true
        viewModelScope.launch {
            try{


                val call=CategoryPostRequest(categoryName)
                Log.d("uploadCategory","uploadCategory request--->$categoryName...$call")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().uploadCategories(call)
                }

                Log.d("uploadCategory","uploadCategory response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    in 200..201->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _categoryPostResponse.postValue(responseData)

                            Log.d("getFlashSale", "Caching response data: $responseData")
                        } else {
                            _categoryPostResponseError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _categoryPostResponseError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _categoryPostResponseError.postValue("Unknown error occurred")
                    }

                } }
            catch (e: IOException) {
                Log.e("NetworkError", "Network connection issue: ${e.message}")
                _categoryPostResponseError.postValue("Network connection issue, please try again later.")
            }
            catch(e:Exception){
                _categoryPostResponseError.postValue("Failed to fetch data: ${e.message}")
                Log.e("error", "Failed to fetch data: ${e.message}")
            }
            finally{
                _isLoading.value=false
            }
        }
    }

    fun deleteRecordApi(itemId:String){
        viewModelScope.launch {
            _isLoading.value=true
            Log.d("deleteRecordApi", "deleteRecordApi")
            val response = withContext(Dispatchers.IO) {
                BaseUrlProvider.create().deleteCategory(itemId)
            }
            try {
                Log.d("deleteRecordApi","deleteRecordApi response--->...${response.code()}...${response.body()}")
                when(response.code()){
                    in 200 ..201->{
                        if (response.body() != null) {
                            _categoryDeleteResponse.postValue(response.body())

                        } else {
                            _categoryDeleteError.postValue("Empty data")
                        }
                    }
                    in 400..500->{
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _categoryDeleteError.postValue(errorMessage)
                        }
                    }
                    else->{
                        Log.d("response","--->error${response.code()}")
                        _categoryDeleteError.postValue("Unknown error occurred")
                    }
                }
            }  catch (e: IOException) {
                Log.e("NetworkError", "Network connection issue: ${e.message}")
                _categoryDeleteError.postValue("Network connection issue, please try again later.")
            }
            catch(e:Exception){
                _categoryDeleteError.postValue("Failed to fetch data: ${e.message}")
                Log.e("error", "Failed to fetch data: ${e.message}")
            }finally {
                _isLoading.value=false
            }
        }

    }
    fun editCategory(categoryID:String,categoryName:String){

        _isLoading.value=true
        viewModelScope.launch {
            try{
                val call=CategoryPostRequest(categoryName)
                Log.d("editCategory","editCategory request--->$categoryName...$call")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().editCategory(categoryID,call)
                }

                Log.d("editCategory","editCategory response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    in 200..201->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _categoryUpdateResponse.postValue(responseData)


                        } else {
                            _categoryUpdateResponseError.postValue("Empty data")
                        }
                    }
                    in 400..500 -> {
                        Extensions.handleErrorResponse(response.errorBody()) { errorMessage ->
                            _categoryUpdateResponseError.postValue(errorMessage)
                        }
                    }
                    else -> {
                        Log.d("response","--->error${response.code()}")
                        _categoryUpdateResponseError.postValue("Unknown error occurred")
                    }

                } }
            catch (e: IOException) {
                Log.e("NetworkError", "Network connection issue: ${e.message}")
                _categoryUpdateResponseError.postValue("Network connection issue, please try again later.")
            }
            catch(e:Exception){
                _categoryUpdateResponseError.postValue("Failed to fetch data: ${e.message}")
                Log.e("error", "Failed to fetch data: ${e.message}")
            }
            finally{
                _isLoading.value=false
            }
        }
    }

}
