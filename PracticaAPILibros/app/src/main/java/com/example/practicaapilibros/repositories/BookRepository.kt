package com.example.practicaapilibros.repositories

import com.example.practicaapilibros.api.LibreriaAPI
import com.example.practicaapilibros.api.models.Book
import com.example.practicaapilibros.api.models.BookList
import com.example.practicaapilibros.api.models.BookRequestDTO
import retrofit2.create

class BookRepository {
    private val service: LibreriaAPI by lazy {
        RetrofitRepository.getInstance().create<LibreriaAPI>()
    }

    suspend fun getBookList(): BookList {
        return service.getBookList()
    }

    suspend fun getBook(id: Int): Book {
        return service.getBook(id)
    }

    suspend fun createBook(book: BookRequestDTO): Book {
        return service.createBook(book)
    }

    suspend fun updateBook(id: Int, book: BookRequestDTO): Book {
        return service.updateBook(id, book)
    }

    suspend fun deleteBook(id: Int) {
        service.deleteBook(id)
    }
}
