package com.example.bookstoremvvmcoroutines.data

interface BooksRepository {
    suspend fun getBooks(): NetworkResult<List<Book>>
    suspend fun addBook(book: Book): NetworkResult<Book>
}