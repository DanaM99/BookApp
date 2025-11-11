package com.example.bookapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookapp.viewmodel.SavedBooksViewModel
import androidx.compose.ui.unit.dp


@Composable
fun SavedBooksScreen(viewModel: SavedBooksViewModel = viewModel()) {

    val books by viewModel.savedBooks.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(text = "Mis libros guardados", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        if (books.isEmpty()) {
            Text("Todavía no guardaste ningún libro.")
        } else {
            LazyColumn {
                items(books) { book ->
                    Text(text = book.volumeInfo.title ?: "Sin título")
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
