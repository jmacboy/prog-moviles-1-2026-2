package com.example.practicawebapi.api

import com.example.practicawebapi.api.models.Post
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceholderAPI {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): Post
}