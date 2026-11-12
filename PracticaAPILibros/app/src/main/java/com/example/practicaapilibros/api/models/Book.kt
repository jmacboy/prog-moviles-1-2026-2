package com.example.practicaapilibros.api.models

typealias BookList = ArrayList<Book>

data class Book(
    val id: Int,
    val nombre: String,
    val autor: String,
    val editorial: String,
    val imagen: String,
    val sinopsis: String,
    val isbn: String,
    val calificacion: Int,
    val generos: List<Genero>,
    val created_at: String,
    val updated_at: String
)

data class Genero(
    val id: Int,
    val nombre: String,
    val pivot: Pivot,
    val created_at: String,
    val updated_at: String
)

data class Pivot(
    val genero_id: Int,
    val libro_id: Int
)