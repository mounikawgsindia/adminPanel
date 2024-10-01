package com.wingspan.adminpanel.model

data class RegistrationRequest( val fullname:String?,val username:String?,val email:String?,val phnumber:String?,
                                val password:String?,val shopname:String?, val shopaddress:String?,val shoppincode:String?,
                                val gstnumber:String?,val latitude:String?,val longitude:String?)
data class LoginRequest(val username:String,val password:String)
//data class FlashSalePostModel(val title:String?,val description:String?,val quantity:String?,val price:String?,val dprice:String?,
                          //    val :String?, val :String?)
