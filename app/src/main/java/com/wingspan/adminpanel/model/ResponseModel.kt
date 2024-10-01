package com.wingspan.adminpanel.model

import android.location.Location
import android.view.WindowInsetsAnimation



data class ResponseData(var message:String?)
data class ErrorResponse(val status: Boolean, val error: String,val message:String)
data class LoginResponse(var message:String)
data class FlashSaleResponse(var id:String?, var title:String?, var description:String?, var dprice:String?, var discount:String?, var price:String?,var image:String?)
data class ApprovedMerchants(var id:String?,val fullname:String?,val username:String?,val email:String?,val phnumber:String?,val shopname:String?,val shopaddress:String?,val gstnumber:String?)
data class RejectedMerchants(var id:String?,val fullname:String?,val username:String?,val email:String?,val phnumber:String?,val shopname:String?,val shopaddress:String?,val gstnumber:String?)
data class AwaitingMerchants(var id:String?,val fullname:String?,val username:String?,val email:String?,val phnumber:String?,val shopname:String?,val shopaddress:String?,val gstnumber:String?)