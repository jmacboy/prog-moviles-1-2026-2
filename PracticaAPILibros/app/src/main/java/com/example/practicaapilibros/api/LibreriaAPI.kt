package com.example.practicaapilibros.api

import com.example.practicaapilibros.api.models.Book
import com.example.practicaapilibros.api.models.BookList
import com.example.practicaapilibros.api.models.BookRequestDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LibreriaAPI {
    @GET("libros")
    suspend fun getBookList(): BookList

    @GET("libros/{id}")
    suspend fun getBook(@Path("id") id: Int): Book

    @POST("libros")
    suspend fun createBook(@Body book: BookRequestDTO): Book

    @PUT("libros/{id}")
    suspend fun updateBook(@Path("id") id: Int, @Body book: BookRequestDTO): Book

    @DELETE("libros/{id}")
    suspend fun deleteBook(@Path("id") id: Int)
}
