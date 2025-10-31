package com.example.bookapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.bookapp.Screen
import com.example.bookapp.viewmodel.BooksViewModel
import com.google.gson.Gson

@Composable
fun SearchBooksScreen(navController: NavController) {
    val viewModel: BooksViewModel = viewModel()
    val books = viewModel.books.collectAsState()
    var query by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar libro") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = { viewModel.searchBooks(query) }) {
            Text("Buscar")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(books.value) { book ->
                Row(
                    Modifier
                        .padding(8.dp)
                        .clickable {
                            val bookJson = Gson().toJson(book)
                            navController.navigate("book_detail/${java.net.URLEncoder.encode(bookJson, "UTF-8")}")
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(book.volumeInfo.imageLinks?.thumbnail),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(book.volumeInfo.title ?: "Sin título", style = MaterialTheme.typography.bodyLarge)
                        Text(book.volumeInfo.authors?.joinToString() ?: "Autor desconocido")
                    }
                }
                Divider()
            }
        }
    }
}
