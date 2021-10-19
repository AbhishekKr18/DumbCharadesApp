package com.kr.abhishek.guessthemovie.model

import com.squareup.moshi.Json

data class Movies(
    val page : Int,

    @Json(name = "results") val movieDetails : List<MovieDetails>,

    val total_pages : Int,

    val total_results : Int
)