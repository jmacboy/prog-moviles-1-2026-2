package com.example.practicawebapi.api

import com.example.practicawebapi.api.models.Post
import retrofit2.http.GET

interface JsonPlaceholderAPI {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}