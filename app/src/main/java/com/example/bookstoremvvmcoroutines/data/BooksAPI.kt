package com.example.bookstoremvvmcoroutines.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BooksAPI {
    @GET("books")
    suspend fun getBooks(): Response<List<Book>>

    @POST("books")
    suspend fun addBook(@Body book: Book): Response<Book>

    @DELETE("books/{bookId}")
    suspend fun deleteBook(@Path("bookId") bookId: Int): Response<Book>

    @PUT("books/{bookId}")
    suspend fun updateBook(@Path("bookId") bookId: Int, @Body book: Book): Response<Book>
}