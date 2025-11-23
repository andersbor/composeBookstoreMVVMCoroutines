package com.example.bookstoremvvmcoroutines

sealed class NavRoutes(val route: String) {
    data object BookList : NavRoutes("list")
    data object BookDetails : NavRoutes("details")
    data object BookAdd : NavRoutes("add")
}