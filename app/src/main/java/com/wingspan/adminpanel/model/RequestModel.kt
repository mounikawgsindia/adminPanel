package com.wingspan.adminpanel.model

data class RegistrationRequest( val firstName:String?,val lastName:String?,val username:String?,val mobileNumber:String?,val email:String?,
                                val password:String?)
data class LoginRequest(val username:String,val password:String)
data class CategoriesModel(var categorie_id:Int?,val categorie_name:String?)
data class CategoryPostRequest(var categorie_name:String?)
//data class FlashSalePostModel(val title:String?,val description:String?,val quantity:String?,val price:String?,val dprice:String?,
                          //    val :String?, val :String?)
