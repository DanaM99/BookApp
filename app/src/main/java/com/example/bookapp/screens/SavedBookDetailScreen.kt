package com.example.bookapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.bookapp.viewmodel.SavedBooksViewModel

@Composable
fun SavedBookDetailScreen(
    bookId: String,
    viewModel: SavedBooksViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    val book = viewModel.getBookById(bookId).collectAsState(initial = null).value

    if (book == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando...")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val thumbnail = book.volumeInfo.imageLinks?.thumbnail
        if (thumbnail != null) {
            Image(
                painter = rememberAsyncImagePainter(thumbnail),
                contentDescription = book.volumeInfo.title,
                modifier = Modifier
                    .size(180.dp)
                    .padding(12.dp)
            )
        }

        Text(
            text = book.volumeInfo.title ?: "Sin título",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = book.volumeInfo.authors?.joinToString(", ") ?: "Autor desconocido",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Páginas: ${book.volumeInfo.pageCount ?: "?"}",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botones
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                viewModel.deleteBook(bookId)
                onBack()
            }) {
                Text("Quitar de mi biblioteca")
            }

            OutlinedButton(onClick = {
                // Navegar o abrir modal de reseña
            }) {
                Text("Añadir reseña")
            }
        }
    }
}
