package com.example.bookstoremvvmcoroutines.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    @SerialName("id")
    // TODO this is kotlinx annotation
    val id: Int = -1,
    @SerialName("title")
    val title: String,
    @SerialName("price")
    val price: Double
)
