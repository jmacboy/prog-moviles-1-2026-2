package com.example.practicawebapi.api.models

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)