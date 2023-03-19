package com.example.movieapp.Data.API

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "6e63c2317fbe963d76c3bdc2b785f6d1"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_PATH = "https://image.tmdb.org/t/p/w342/"

object MovieDBClient {
    fun getClient(): MovieDBInterface {
        /** Interceptor is an Observer*/
        val requestInterceptor = Interceptor { chain ->
            /** Add api_key to my url */
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            /** request */
            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        /** Initialize okHttp client */
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .callTimeout(10, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create();

        //return RetrofitClient
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MovieDBInterface::class.java)
    }
}