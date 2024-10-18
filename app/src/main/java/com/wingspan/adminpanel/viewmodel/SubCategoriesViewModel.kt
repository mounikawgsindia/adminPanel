package com.wingspan.adminpanel.viewmodel

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
import com.wingspan.adminpanel.model.SubCategoriesModel
import com.wingspan.adminpanel.model.SubCategoryPostRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubCategoriesViewModel: ViewModel() {


    private val _categoryResponse= MutableLiveData<List<SubCategoriesModel>?>()
    val categoryResponse: LiveData<List<SubCategoriesModel>?> get() =_categoryResponse

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


    fun getSubCategories(categoryID:String){


        _isLoading.value=true
        viewModelScope.launch {
            try{

                Log.d("getCategories","getCategories request--->")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().getSubCategories()
                }

                Log.d("getCategories","getCategories response--->...${response.code()}...${response.body()}")
                when (response.code()){
                    in 200..201->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _categoryResponse.postValue(responseData)

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
            catch(e:Exception){
                _categoryResponseError.postValue("Please check your NetWork Connection")

                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                _isLoading.value=false
            }
        }
    }
    fun uploadSubCategory(categoryId:String,categoryName:String){
        _isLoading.value=true
        viewModelScope.launch {
            try{


                val call= SubCategoryPostRequest(categoryName,categoryId)
                Log.d("uploadSubCategory","uploadSubCategory request--->$categoryName...$call")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().uploadSubCategories(call)
                }

                Log.d("uploadSubCategory","uploadSubCategory response--->...${response.code()}...${response.body()}..${response.errorBody()?.string()}")
                when (response.code()){
                    in 200..201->{
                        val responseData = response.body()
                        // Cache the response

                        if (responseData != null) {
                            _categoryPostResponse.postValue(responseData)

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
            catch(e:Exception){
                _categoryPostResponseError.postValue("Please check your NetWork Connection")

                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                _isLoading.value=false
            }
        }
    }


    fun deleteRecordApi(itemId:String){
        viewModelScope.launch {
            _isLoading.value=true
            Log.d("deleteRecordApi", "deleteRecordApi ${itemId}")
            val response = withContext(Dispatchers.IO) {
                BaseUrlProvider.create().deleteSubCategory(itemId)
            }
            try {
                Log.d("deleteRecordApi","deleteRecordApi response--->...${response.code()}...${response.body()}..${response.errorBody()?.string()}")
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
            } catch (e: Exception) {
                _categoryDeleteError.postValue("Please check your NetWork Connection")
            } finally {
                _isLoading.value=false
            }
        }

    }
    fun editSubCategory(categoryID:String,categoryName:String,itemId:String){


        _isLoading.value=true
        viewModelScope.launch {
            try{
                val call= SubCategoryPostRequest(categoryName,categoryID)
                Log.d("editCategory","editCategory request--->$categoryName...$call")
                val response= withContext(Dispatchers.IO){
                    BaseUrlProvider.create().editSubCategory(itemId,call)
                }

                Log.d("editCategory","editCategory response--->...${response.code()}...${response.body()}..${response.errorBody()?.string()}")
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
            catch(e:Exception){
                _categoryUpdateResponseError.postValue("Please check your NetWork Connection")

                Log.e("error", "Failed to fetch data:NetWork Issue ${e.message}")
            }
            finally{
                _isLoading.value=false
            }
        }
    }

}
