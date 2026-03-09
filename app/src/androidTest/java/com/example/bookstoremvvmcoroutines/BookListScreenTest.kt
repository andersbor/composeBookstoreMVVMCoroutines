package com.example.bookstoremvvmcoroutines

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookstoremvvmcoroutines.data.Book
import com.example.bookstoremvvmcoroutines.views.BookListScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BookListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bookListErrorMessageTest() {
        composeTestRule.setContent {
            val booksUIState = BooksUIState(
                isLoading = false,
                books = emptyList(), error = "Error message"
            )
            BookListScreen(booksUIState = booksUIState)
        }

        composeTestRule.onNodeWithText("Problem", substring = true).assertIsDisplayed()
    }

    @Test
    fun bookListTest() {
        val book1 = Book(id = 1, title = "Book1", price = 9.95)
        val book2 = Book(id = 2, title = "Book2", price = 14.33)
        val myBooks = listOf(book1, book2)
        val booksUIState = BooksUIState(
            isLoading = false,
            books = myBooks, error = null
        )
        composeTestRule.setContent {
            BookListScreen(booksUIState = booksUIState)
        }

        composeTestRule.onNodeWithText("Problem", substring = true).assertDoesNotExist()

        composeTestRule.onNodeWithText("Book1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Book2", substring = true).assertIsDisplayed()
    }
}