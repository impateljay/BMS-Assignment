package com.jay.bmsassignment.network

import com.jay.bmsassignment.model.Tweet
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import java.util.concurrent.TimeUnit

interface ApiInterface {

    @Headers("Accept: application/json")
    @GET("search/tweets.json")
    fun getTweets(
        @Query("q") searchText: String, @Query("result_type") resultType: String,
        @Query("count") count: Int, @Query("tweet_mode") tweetMode: String
    ): Call<Tweet>

    companion object Factory {
        fun create(): ApiInterface {
            val consumer =
                OkHttpOAuthConsumer(
                    "uzZ1eE651FHhTiTx838tEJSCt",
                    "Ej1xV1Y9XJocqc4ia3dKBV2A0VcxznkPPPzQnXLHUzE0qUGbLh"
                )
            consumer.setTokenWithSecret(
                "1079624344265998337-1KjyLJ6Lwoy9cWAxq28zOtJM7LIa0B",
                "mZYO3KR7ys3HSxCKuNH16NqPbPlW3ShvPMa5uiNqz5FXa"
            )

            val okHttpClient = OkHttpClient.Builder()
                .writeTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .addInterceptor(SigningInterceptor(consumer))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.twitter.com/1.1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}