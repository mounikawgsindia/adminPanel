package com.wingspan.adminpanel.api


import com.wingspan.adminpanel.model.ApprovedFlashSale
import com.wingspan.adminpanel.model.ApprovedMerchants
import com.wingspan.adminpanel.model.AwaitingFlashSale
import com.wingspan.adminpanel.model.AwaitingMerchants
import com.wingspan.adminpanel.model.FlashSaleResponse
import com.wingspan.adminpanel.model.LoginRequest
import com.wingspan.adminpanel.model.LoginResponse
import com.wingspan.adminpanel.model.RegistrationRequest
import com.wingspan.adminpanel.model.RejectedFlashSale
import com.wingspan.adminpanel.model.RejectedMerchants
import com.wingspan.adminpanel.model.ResponseData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface EndPointUrlProvider {

    //signup
    @POST("head/signup")
    @Headers("Content-Type:application/json")
   suspend fun registerUser(@Body registrationData: RegistrationRequest):Response<ResponseData>

   //login
    @POST("head/login")
    @Headers("Content-Type:application/json")
    suspend fun loginUser(@Body registrationData: LoginRequest):Response<LoginResponse>




    @GET("giving/approved-shopkeepers")
    suspend fun approvedMerchants():Response<List<ApprovedMerchants>>

    @GET("giving/pending-shopkeepers")
    suspend fun awaitingMerchants():Response<List<AwaitingMerchants>>

    @GET("giving/rejected-shopkeepers")
    suspend fun rejectedMerchants():Response<List<RejectedMerchants>>





    @POST("giving/reject-shopkeeper/{id}")
    suspend fun postRejectShopkeeper(
        @Header("Authorization") authorization:String,
        @Path("id") id:String):Response<ResponseData>


    @POST("giving/approve-shopkeeper/{id}")
    suspend fun postAcceptShopkeeper(
        @Header("Authorization") authorization:String,
        @Path("id") id:String):Response<ResponseData>

//flash sale apis

    @GET("shopkeeper/getting")
    suspend fun approvedFlashSale():Response<List<ApprovedFlashSale>>

    @GET("admin/pending")
    suspend fun awaitingFlashSale():Response<List<AwaitingFlashSale>>

    @GET("admin/allrejected")
    suspend fun rejectedFlashSale():Response<List<RejectedFlashSale>>

    @PUT("admin/reject/{id}")
    suspend fun getRejectFlashSaleById(
        @Header("Authorization") authorization:String,
        @Path("id") id:String):Response<ResponseData>


    @PUT("admin/approve/{id}")
    suspend fun getAcceptFlashSaleById(
        @Header("Authorization") authorization:String,
        @Path("id") id:String):Response<ResponseData>

    @PUT("admin/approve-all")
    suspend fun allApproveFlashSale():Response<ResponseData>

}