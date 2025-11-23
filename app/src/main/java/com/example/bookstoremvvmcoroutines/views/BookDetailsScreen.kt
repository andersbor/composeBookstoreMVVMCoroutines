package com.example.bookstoremvvmcoroutines.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.KeyboardType
import com.example.bookstoremvvmcoroutines.data.Book

@OptIn(ExperimentalMaterial3Api::class) // TopAppBar
@Composable
fun BookDetailsScreen(
    book: Book, modifier: Modifier = Modifier,
    onUpdate: (Int, Book) -> Unit = { id: Int, data: Book -> },
    onNavigateBack: () -> Unit = {}
) {
    var title by remember(book) { mutableStateOf(book.title) }
    var priceStr by remember(book) { mutableStateOf(book.price.toString()) }
    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Book details") })
        }) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            // TODO layout for landscape
            // TODO add and details are very similar
            OutlinedTextField(onValueChange = { title = it },
                value = title,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Title") })
            OutlinedTextField(onValueChange = { priceStr = it },
                value = priceStr,
                // https://medium.com/@GkhKaya00/exploring-keyboard-types-in-kotlin-jetpack-compose-ca1f617e1109
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Price") })
            Row(
                modifier = modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onNavigateBack() }) {
                    Text("Back")
                }
                Button(onClick = {
                    val price = priceStr.toDoubleOrNull()
                    if (title.isNotBlank() && price != null) {
                        val data = Book(title = title, price = priceStr.toDouble())
                        onUpdate(book.id, data)
                    }
                    else {
                        // TODO show error similar to AddScreen
                    }
                }) {
                    Text("Update")
                }
            }
        }
    }
}