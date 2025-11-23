package com.example.bookstoremvvmcoroutines.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BooksAPI {
    @GET("books")
    suspend fun getBooks(): Response<List<Book>>

    @POST("books")
    suspend fun addBook(@Body book: Book): Response<Book>
}