package com.example.bookstoremvvmcoroutines.views

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bookstoremvvmcoroutines.data.Book

@OptIn(ExperimentalMaterial3Api::class) // TopAppBar
@Composable
fun BookAddScreen(
    modifier: Modifier = Modifier,
    addBook: (Book) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var priceStr by remember { mutableStateOf("") }
    var titleIsError by remember { mutableStateOf(false) }
    var priceIsError by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Add a book") })
        }) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding).padding(8.dp)) {
            // TODO show error message
            val orientation = LocalConfiguration.current.orientation
            val isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT
            // TODO refactor duplicated code: component InputField?
            if (isPortrait) {
                OutlinedTextField(onValueChange = { title = it },
                    value = title,
                    isError = titleIsError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Title") })
                OutlinedTextField(onValueChange = { priceStr = it },
                    value = priceStr,
                    // https://medium.com/@GkhKaya00/exploring-keyboard-types-in-kotlin-jetpack-compose-ca1f617e1109
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = priceIsError,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Price") })
            } else {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedTextField(onValueChange = { title = it },
                        value = title,
                        isError = titleIsError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Title") })
                    OutlinedTextField(onValueChange = { priceStr = it },
                        value = priceStr,
                        // https://medium.com/@GkhKaya00/exploring-keyboard-types-in-kotlin-jetpack-compose-ca1f617e1109
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = priceIsError,
                        modifier = Modifier.weight(1f),
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
                    if (title.isEmpty()) {
                        titleIsError = true
                        return@Button
                    }
                    if (priceStr.isEmpty()) {
                        priceIsError = true
                        return@Button
                    }
                    val price = priceStr.toDoubleOrNull()
                    if (price == null) {
                        priceIsError = true
                        return@Button
                    }
                    val book = Book(title = title, price = price)
                    addBook(book)
                    //onNavigateBack()
                }) {
                    Text("Add")
                }
            }
        }
    }
}