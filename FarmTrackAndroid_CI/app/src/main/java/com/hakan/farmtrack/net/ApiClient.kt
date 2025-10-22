package com.hakan.farmtrack.net

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient{
    fun create(base: String): Api{
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val client = OkHttpClient.Builder().addInterceptor(logger).build()
        val moshi = Moshi.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(if(base.endsWith("/")) base else base + "/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
        return retrofit.create(Api::class.java)
    }
}
