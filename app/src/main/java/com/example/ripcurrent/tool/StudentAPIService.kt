package com.example.ripcurrent.tool

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
//    @GET("endpoint")
//    fun getData(
//        @Query("param1") param1: String,
//        @Query("param2") param2: Int
//    ): Call<ResponseModel>
//    // 學生資料查詢，使用Post，寄送參數，回傳參數
//    @POST("query/verify") suspend fun authentication(@Body apiService: ApiService): Response<StudentInformationResponse>
//
//    // 學生帳號新增
//    @POST("insert/newaccount") suspend fun createaccount(@Body apiService: ApiService): Response<StudentAccountResponse>
//
//    // 學生帳號登入
//    @POST("query/checkaccount") suspend fun checkaccount(@Body apiService: ApiService): Response<AccountCheckResponse>
}