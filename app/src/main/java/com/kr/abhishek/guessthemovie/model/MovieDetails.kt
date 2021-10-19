package com.kr.abhishek.guessthemovie.model

import androidx.annotation.Nullable
import com.squareup.moshi.Json

data class MovieDetails(
    @Nullable
    @Json(name = "poster_path") val image_src : String,
    val title : String
)
