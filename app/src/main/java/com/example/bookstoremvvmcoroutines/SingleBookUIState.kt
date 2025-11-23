package com.example.bookstoremvvmcoroutines

import com.example.bookstoremvvmcoroutines.data.Book

data class SingleBookUIState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val error: String? = null
)
