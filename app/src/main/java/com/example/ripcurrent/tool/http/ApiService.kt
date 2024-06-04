package com.example.ripcurrent.tool.http

import com.example.ripcurrent.Data.Member
import com.example.ripcurrent.Data.MemberResponse
import com.example.ripcurrent.Data.PhotoInfoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Streaming

interface ApiService {
    @Multipart
    @POST("photo/post")
    suspend fun uploadImage(@Part filePart: MultipartBody.Part,
                            @Part("information") jsonPart: RequestBody
    ): Response<ResponseBody>
    @POST("member/insert")
    suspend fun insertMember(@Body member: Member): Response<MemberResponse>
    @POST("photo/get/one")
    suspend fun getPhotoInfo(@Body member: Member): Response<MemberResponse>
    @POST("member/query/login")
    suspend fun loginMember(@Body member: Member): Response<MemberResponse>
    @POST("member/modify")
    suspend fun updateMember(@Body member: Member): Response<MemberResponse>
    @Streaming
    @GET("photo/get/folder")
    suspend fun getImages(): Response<ResponseBody>
    @GET("photo/get/folder/information")
    suspend fun getImagesInfo(): Response<List<PhotoInfoResponse>>
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
//LaunchedEffect(Unit) {}
// CoroutineScope(Dispatchers.Main).launch {}
// runBlocking {}