package com.example.bookstoremvvmcoroutines.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bookstoremvvmcoroutines.data.Book

@OptIn(ExperimentalMaterial3Api::class) // TopAppBar
@Composable
fun BookDetailsScreen(
    book: Book, modifier: Modifier = Modifier,
    onUpdate: (bookId: Int, bookData: Book) -> Unit = { _, _ -> },
    onNavigateBack: () -> Unit = {}
) {
    var titleInput by remember(book) { mutableStateOf(book.title) }
    var priceInput by remember(book) { mutableStateOf(book.price.toString()) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Book details") })
        }) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // TODO add and details are very similar
            val configuration = LocalConfiguration.current
            val columnCount =
                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT
                ) 1 else 2
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                columns = GridCells.Fixed(columnCount)
            ) {
                item {
                    OutlinedTextField(
                        onValueChange = { titleInput = it },
                        value = titleInput,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        label = { Text(text = "Title") })
                }
                item {
                    OutlinedTextField(
                        onValueChange = { priceInput = it },
                        value = priceInput,
                        // https://medium.com/@GkhKaya00/exploring-keyboard-types-in-kotlin-jetpack-compose-ca1f617e1109
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        label = { Text(text = "Price") })
                }
            }
            Row(
                modifier = modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onNavigateBack() }) {
                    Text("Back")
                }
                Button(onClick = {
                    val price = priceInput.toDoubleOrNull()
                    if (titleInput.isNotBlank() && price != null) {
                        val bookData = Book(title = titleInput, price = priceInput.toDouble())
                        onUpdate(book.id, bookData)
                    } else {
                        // TODO show error similar to AddScreen
                    }
                }) {
                    Text("Update")
                }
            }
        }
    }
}