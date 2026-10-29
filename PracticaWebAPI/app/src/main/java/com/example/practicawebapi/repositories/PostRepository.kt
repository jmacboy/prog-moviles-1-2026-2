package com.example.practicawebapi.repositories

import com.example.practicawebapi.api.JsonPlaceholderAPI
import com.example.practicawebapi.api.models.Post
import retrofit2.create

class PostRepository {
    suspend fun getPostList(): List<Post> {
        val service = RetrofitRepository.getInstance()
            .create<JsonPlaceholderAPI>()
        return service.getPosts()
    }
    suspend fun getPostById(id: Int): Post {
        val service = RetrofitRepository.getInstance()
            .create<JsonPlaceholderAPI>()
        return service.getPostById(id)
    }
}