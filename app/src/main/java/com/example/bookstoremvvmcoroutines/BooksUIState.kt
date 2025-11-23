package com.example.bookstoremvvmcoroutines

import com.example.bookstoremvvmcoroutines.data.Book

data class BooksUIState (
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null
)