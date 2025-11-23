package com.example.bookstoremvvmcoroutines.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.bookstoremvvmcoroutines.BooksUIState
import com.example.bookstoremvvmcoroutines.data.Book

@OptIn(ExperimentalMaterial3Api::class) // TopAppBar
@Composable
fun BookListScreen(
    booksUIState: BooksUIState,
    modifier: Modifier = Modifier,
    onAdd: () -> Unit = {},
    onBookSelect: (Book) -> Unit = {},
    onBooksReload: () -> Unit = {},
    onBookDelete: (Book) -> Unit = {},
    filterByTitle: (String) -> Unit = {},
    sortByTitle: (Boolean) -> Unit = {},
    sortByPrice: (Boolean) -> Unit = {}
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
            onBookSelected = onBookSelect,
            onBooksReload = onBooksReload,
            onBookDelete = onBookDelete,
            onFilterByTitle = filterByTitle,
            sortByTitle = sortByTitle,
            sortByPrice = sortByPrice
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class) // PullToRefreshBox
@Composable
fun BookListPanel(
    booksUIState: BooksUIState,
    modifier: Modifier = Modifier,
    onBookSelected: (Book) -> Unit = {},
    onBooksReload: () -> Unit = {},
    onBookDelete: (Book) -> Unit = {},
    onFilterByTitle: (String) -> Unit = {},
    sortByTitle: (Boolean) -> Unit = {},
    sortByPrice: (Boolean) -> Unit = {}
) {
    var sortTitleAscending by remember { mutableStateOf(true) }
    var sortPriceAscending by remember { mutableStateOf(true) }
    var titleFragment by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = titleFragment,
                onValueChange = { titleFragment = it },
                label = { Text("Filter by title") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (titleFragment.isNotEmpty()) {
                        IconButton(onClick = {
                            titleFragment = ""
                            onFilterByTitle("") // Clear filter immediately
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onFilterByTitle(titleFragment)
                    keyboardController?.hide()
                })
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        sortByTitle(sortTitleAscending)
                        sortTitleAscending = !sortTitleAscending
                    }) {
                    Text(text = "Title")
                    Icon(
                        imageVector =
                            if (sortTitleAscending) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp,
                        contentDescription =
                            if (sortTitleAscending) "Sort by title descending" else "Sort by title ascending",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        sortByPrice(sortPriceAscending)
                        sortPriceAscending = !sortPriceAscending
                    }) {
                    Text(text = "Price")
                    Icon(
                        imageVector = if (sortPriceAscending) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = if (sortPriceAscending) "Sort Descending" else "Sort Ascending",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (booksUIState.isLoading) {
                    CircularProgressIndicator()
                } else if (booksUIState.error != null) {
                    Text(
                        text = booksUIState.error ?: "Unknown Error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                } else if (booksUIState.books.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Text(text = "No books found", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    // https://developer.android.com/develop/ui/compose/components/pull-to-refresh
                    PullToRefreshBox(
                        isRefreshing = booksUIState.isLoading,
                        onRefresh = { onBooksReload() },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn {
                            items(booksUIState.books, key = { book -> book.id })
                            { book ->
                                BookListItem(
                                    book = book, onBookSelected = onBookSelected,
                                    onBookDeleted = onBookDelete
                                )
                            }
                        }
                    }
                }
            }
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
        ) {
            ListItem(
                colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent),
                headlineContent = {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = {
                    Text(
                        text = "ID: ${book.id}",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${book.price}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        IconButton(onClick = { onBookDeleted(book) }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete ${book.title}",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    }
}