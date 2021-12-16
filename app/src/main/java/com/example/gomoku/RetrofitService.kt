package com.example.gomoku


import com.google.gson.GsonBuilder
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers


var url1 = "https://api.unsplash.com"

interface RetrofitService {
    @Headers("Authorization: Client-ID QbUZoYtCf_uq8BUKh6UvwQy6zu9OKN5UcBU5_6h-jfo")
    @GET("photos/random?orientation=portrait")
    fun getRandomPhoto(): Call<String>

    companion object {
        fun create(url: String = url1): RetrofitService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(
                    ScalarsConverterFactory.create()
                ).addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLenient().create()
                    )
                )
                .baseUrl(url)
                .build()
            return retrofit.create(RetrofitService::class.java)
        }

    }
}