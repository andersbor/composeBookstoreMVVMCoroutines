package com.example.bookstoremvvmcoroutines.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.text.input.ImeAction
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Add a book") })
        }) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val orientation = LocalConfiguration.current.orientation
            val isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT
            // TODO refactor duplicated code: component InputField?
            if (isPortrait) {
                OutlinedTextField(
                    onValueChange = {
                        title = it
                        titleIsError = false
                    },
                    value = title,
                    isError = titleIsError,
                    supportingText = {
                        if (titleIsError) {
                            Text(text = "Title is required")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next // Move to next field
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Title") })
                OutlinedTextField(
                    onValueChange = {
                        priceStr = it
                        priceIsError = false
                    },
                    value = priceStr,
                    // https://medium.com/@GkhKaya00/exploring-keyboard-types-in-kotlin-jetpack-compose-ca1f617e1109
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done // Submit
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        // Trigger update logic here
                    }),
                    isError = priceIsError,
                    supportingText = {
                        if (priceIsError) {
                            Text(text = "Price is required")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Price") }
                )
            } else {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedTextField(
                        onValueChange = { title = it },
                        value = title,
                        isError = titleIsError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Title") }
                    )
                    OutlinedTextField(
                        onValueChange = { priceStr = it },
                        value = priceStr,
                        // https://medium.com/@GkhKaya00/exploring-keyboard-types-in-kotlin-jetpack-compose-ca1f617e1109
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done // Submit
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            // Trigger update logic here
                        }),
                        isError = priceIsError,
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Price") }
                    )
                }
            }
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
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
                }) {
                    Text("Add")
                }
            }
        }
    }
}