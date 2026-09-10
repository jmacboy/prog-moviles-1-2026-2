package com.example.practicainstagram

class Post(
    val profileId: Int, //id de la foto de perfil
    val userName: String,
    val imageUrl: String, //url de la imagen
    val reactionName: String,
    val date: String,
    val description: String,
    var isLiked: Boolean = false
)