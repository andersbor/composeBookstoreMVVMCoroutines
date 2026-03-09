package com.example.bookstoremvvmcoroutines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookstoremvvmcoroutines.data.Book
import com.example.bookstoremvvmcoroutines.ui.theme.BookstoreMVVMCoroutinesTheme
import com.example.bookstoremvvmcoroutines.viewmodel.BooksViewModel
import com.example.bookstoremvvmcoroutines.views.BookAddScreen
import com.example.bookstoremvvmcoroutines.views.BookDetailsScreen
import com.example.bookstoremvvmcoroutines.views.BookListScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookstoreMVVMCoroutinesTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val booksViewModel: BooksViewModel = koinViewModel() // from dependency injection
    val booksUIState: BooksUIState by booksViewModel.booksUIState.collectAsStateWithLifecycle()
    NavHost(navController = navController, startDestination = NavRoutes.BookList.route) {
        composable(NavRoutes.BookList.route) {
            BookListScreen(
                booksUIState = booksUIState,
                modifier = modifier,
                onAdd = { navController.navigate(NavRoutes.BookAdd.route) },
                onBookSelect =
                    { book -> navController.navigate(NavRoutes.BookDetails.route + "/${book.id}") },
                onBooksReload = { booksViewModel.getBooks() },
                onBookDelete = { book ->
                    booksViewModel.deleteBook(book.id)
                },
                filterByTitle = { booksViewModel.filterByTitle(it) },
                sortByTitle = { booksViewModel.sortByTitle(it) },
                sortByPrice = { booksViewModel.sortByPrice(it) }
            )
        }
        composable(
            NavRoutes.BookDetails.route + "/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backstackEntry ->
            val bookId = backstackEntry.arguments?.getInt("bookId")
            val book = booksUIState.books.find { it.id == bookId } ?: Book(
                title = "No book",
                price = 0.0
            )
            BookDetailsScreen(
                book = book,
                onUpdate = { id, data ->
                    booksViewModel.updateBook(id, data)
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() })
        }
        composable(NavRoutes.BookAdd.route) {
            BookAddScreen(
                onNavigateBack = { navController.popBackStack() },
                addBook = { book ->
                    booksViewModel.addBook(book)
                    navController.popBackStack()
                }
            )
        }
    }
}
