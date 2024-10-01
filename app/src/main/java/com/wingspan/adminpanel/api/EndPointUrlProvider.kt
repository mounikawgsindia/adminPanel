package com.wingspan.adminpanel.api


import com.wingspan.adminpanel.model.ApprovedMerchants
import com.wingspan.adminpanel.model.FlashSaleResponse
import com.wingspan.adminpanel.model.LoginRequest
import com.wingspan.adminpanel.model.LoginResponse
import com.wingspan.adminpanel.model.PendingMerchants
import com.wingspan.adminpanel.model.RegistrationRequest
import com.wingspan.adminpanel.model.RejectedMerchants
import com.wingspan.adminpanel.model.ResponseData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface EndPointUrlProvider {

    //signup
    @POST("access/signup")
    @Headers("Content-Type:application/json")
   suspend fun registerUser(@Body registrationData: RegistrationRequest):Response<ResponseData>

   //login
    @POST("access/login")
    @Headers("Content-Type:application/json")
    suspend fun loginUser(@Body registrationData: LoginRequest):Response<LoginResponse>

    //flashsale get
    @GET("shopkeeper/getting")
    @Headers("Content-Type:application/json")
    suspend fun getFlashSale():Response<List<FlashSaleResponse>>
//
//   //get latitude and longitude from address
//    @GET("geocode/json")
//    fun getGeocodingData(
//        @Query("address") address: String,
//        @Query("key") apiKey: String
//    ): Call<GeocodingResponse>

    @GET("giving/approved-shopkeepers")
    suspend fun approvedMerchants():Response<List<ApprovedMerchants>>

    @GET("giving/pending-shopkeepers")
    suspend fun pendingMerchants():Response<List<PendingMerchants>>

    @GET("giving/rejected-shopkeepers")
    suspend fun rejectedMerchants():Response<List<RejectedMerchants>>


    @DELETE("shopkeeper/delete/{id}")
    suspend fun deleteFlashSaleItem(@Path("id")id:String):Response<ResponseData>
    //post flash sale
    @Multipart
    @POST("shopkeeper/upload")
    suspend fun postFlashSaleDetails(@Part("title") title: RequestBody,
                                     @Part("description") description: RequestBody,
                                     @Part("quantity") quantity: RequestBody,
                                     @Part("price") price: RequestBody,
                                     @Part("dprice") dprice: RequestBody,
                                     @Part("discount") discount: RequestBody,
                                     @Part image:MultipartBody.Part):Response<ResponseData>


}