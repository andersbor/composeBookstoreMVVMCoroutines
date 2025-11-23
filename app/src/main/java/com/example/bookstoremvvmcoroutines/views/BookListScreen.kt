package com.example.bookstoremvvmcoroutines.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookstoremvvmcoroutines.BooksUIState
import com.example.bookstoremvvmcoroutines.data.Book

@OptIn(ExperimentalMaterial3Api::class) // TopAppBar
@Composable
fun BookListScreen(
    booksUIState: BooksUIState,
    modifier: Modifier = Modifier,
    onAdd: () -> Unit = {},
    onBookSelected: (Book) -> Unit = {},
    onBooksReload: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Books") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { onAdd() },
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }

    )
    { paddingValues ->
        BookListPanel(
            booksUIState = booksUIState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onBookSelected = onBookSelected,
            onBooksReload = onBooksReload
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class) // PullToRefreshBox
@Composable
fun BookListPanel(
    booksUIState: BooksUIState,
    modifier: Modifier = Modifier,
    onBookSelected: (Book) -> Unit = {},
    onBooksReload: () -> Unit = {}
) {
    //val booksViewModel: BooksViewModel = koinViewModel()
    //val booksUIState: BooksUIState by booksViewModel.booksUIState.collectAsState()

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        //verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = booksUIState.isLoading) {
            CircularProgressIndicator()
        }
        AnimatedVisibility(visible = booksUIState.books.isNotEmpty()) {
            // https://developer.android.com/develop/ui/compose/components/pull-to-refresh
            PullToRefreshBox(
                isRefreshing = booksUIState.isLoading,
                onRefresh = { onBooksReload() },
            ) {
                LazyColumn {
                    items(booksUIState.books, key = { book -> book.id })
                    { book ->
                        BookListItem(book = book, onBookSelected = onBookSelected)
                    }
                }
            }
        }
        AnimatedVisibility(visible = booksUIState.books.isEmpty()) {
            Text(text = "No books")
        }
        AnimatedVisibility(visible = booksUIState.error != null) {
            Text(text = booksUIState.error ?: "ERROR")
        }
    }
}

@Composable
fun BookListItem(
    book: Book,
    modifier: Modifier = Modifier,
    onBookSelected: (Book) -> Unit = {},
    onBookDeleted: (Book) -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxSize(),
        onClick = { onBookSelected(book) }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${book.id} ${book.title}, ${book.price}",
                modifier = Modifier.padding(8.dp)
            )
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Remove " + book.title,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onBookDeleted(book) }
            )
        }
    }
}