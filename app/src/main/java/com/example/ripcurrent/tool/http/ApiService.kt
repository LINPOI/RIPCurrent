package com.example.ripcurrent.tool.http

import com.example.ripcurrent.tool.Data.Member
import com.example.ripcurrent.tool.Data.MemberResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("photo/post")
    suspend fun uploadImage(@Part filePart: MultipartBody.Part): Response<ByteArray>
    @POST("member/insert")
    suspend fun insertMember(@Body member: Member): Response<MemberResponse>
    @POST("member/query/login")
    suspend fun loginMember(@Body member: Member): Response<MemberResponse>
    @POST("member/modify")
    suspend fun updateMember(@Body member: Member): Response<MemberResponse>

//    // 學生資料查詢，使用Post，寄送參數，回傳參數
//    @POST("query/verify") suspend fun authentication(@Body apiService: ApiService): Response<StudentInformationResponse>
//
        // 學生資料查詢，使用Post，寄送參數，回傳參數
//    @POST("photo/post") suspend fun authentication(@Body apiService: ApiService): Response<StudentInformationResponse>
//    // 學生帳號新增
//    @POST("insert/newaccount") suspend fun createaccount(@Body apiService: ApiService): Response<StudentAccountResponse>
//
//    // 學生帳號登入
//    @POST("query/checkaccount") suspend fun checkaccount(@Body apiService: ApiService): Response<AccountCheckResponse>
}