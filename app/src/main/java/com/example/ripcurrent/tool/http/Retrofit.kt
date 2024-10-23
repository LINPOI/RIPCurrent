package com.example.ripcurrent.tool.http


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
const val URL = "http://192.168.159.69/rip_current/"
const val URL2 ="http://192.168.50.159:8080/rip_current/"
const val URL3 ="http://210.70.254.110:2234/rip_current/"
const val SELECTURL= URL2

object Retrofit {

    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    // 攔截器
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    //
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    //

    //
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(SELECTURL)
            .addConverterFactory( MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}

