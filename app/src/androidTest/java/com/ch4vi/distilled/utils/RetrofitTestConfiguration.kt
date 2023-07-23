package com.ch4vi.distilled.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit

class RetrofitTestConfiguration {
    companion object {
        private const val TIMEOUT: Long = 100
    }

    fun client() = initRetrofit(initMoshi(), initClient())

    private fun initMoshi() = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private fun initClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val headers = Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("accept", "application/json")
                .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(headers)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    private fun initRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
}
