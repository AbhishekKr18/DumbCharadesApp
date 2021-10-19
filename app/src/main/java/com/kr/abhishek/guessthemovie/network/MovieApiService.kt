package com.kr.abhishek.guessthemovie.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kr.abhishek.guessthemovie.model.Movies
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/discover/"

 private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


interface MovieApiService {
    @GET("movie?api_key=58afbc75cf40d3ab6f2d5e0f5fad3e1f&with_original_language=hi")
    fun getMovieList(

            @Query("page") page_number : Int
    ) :
            Deferred<Movies>

    @GET("movie?api_key=58afbc75cf40d3ab6f2d5e0f5fad3e1f&with_original_language=hi/")
    fun getTotalPages() : Int
}

//create a object using retrofit to implement the movieapiservice interface
val retrofitService = retrofit.create(MovieApiService::class.java)

val retrofitService2 = retrofit.create(MovieApiService::class.java)
