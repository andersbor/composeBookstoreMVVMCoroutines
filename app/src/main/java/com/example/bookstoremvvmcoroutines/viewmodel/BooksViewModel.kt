package com.example.bookstoremvvmcoroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstoremvvmcoroutines.BooksUIState
import com.example.bookstoremvvmcoroutines.SingleBookUIState
import com.example.bookstoremvvmcoroutines.data.Book
import com.example.bookstoremvvmcoroutines.data.BooksRepository
import com.example.bookstoremvvmcoroutines.data.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BooksViewModel(
    private val booksRepository: BooksRepository
) : ViewModel() {
    private val _booksUIState = MutableStateFlow(BooksUIState())
    val booksUIState: StateFlow<BooksUIState> = _booksUIState

    private val _singleBookUIState = MutableStateFlow(SingleBookUIState())
    val singleBookUIState: StateFlow<SingleBookUIState> = _singleBookUIState

    init {
        getBooks()
    }

    private fun getBooks() {
        _booksUIState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = booksRepository.getBooks()) {
                is NetworkResult.Success -> {
                    _booksUIState.update {
                        it.copy(isLoading = false, books = result.data)
                    }
                }

                is NetworkResult.Error -> {
                    _booksUIState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }
            }
        }
    }

    private fun addBook(book: Book) {
        _singleBookUIState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = booksRepository.addBook(book)) {
                is NetworkResult.Success -> {
                    _singleBookUIState.update {
                        it.copy(isLoading = false, book = result.data)
                    }
                }

                is NetworkResult.Error -> {
                    _singleBookUIState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }
            }
        }
    }
}