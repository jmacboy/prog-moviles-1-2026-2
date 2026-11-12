package com.example.practicaapilibros.api.models

data class BookRequestDTO(
    val nombre: String,
    val autor: String,
    val editorial: String,
    val imagen: String,
    val sinopsis: String,
    val isbn: String,
    val calificacion: Int = 0
)
