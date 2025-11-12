package com.example.bookapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
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

    // Estados locales para reseña y estado del libro
    var status by remember { mutableStateOf(book.status ?: "none") }
    var rating by remember { mutableStateOf(book.rating ?: 0) }
    var comment by remember { mutableStateOf(TextFieldValue(book.comment ?: "")) }

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

        // 🔹 Estado de lectura
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Estado:")
            Switch(
                checked = status == "read",
                onCheckedChange = { checked ->
                    status = if (checked) "read" else "unread"
                }
            )
            Text(if (status == "read") "Leído" else "No leído")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Rating (slider)
        Text("Puntuación: $rating/5")
        Slider(
            value = rating.toFloat(),
            onValueChange = { rating = it.toInt() },
            valueRange = 0f..5f,
            steps = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Comentario
        Text("Comentario:")
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            placeholder = { Text("Escribe tu reseña...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Botones de acción
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                viewModel.deleteBook(bookId)
                onBack()
            }) {
                Text("Quitar de mi biblioteca")
            }

            Button(onClick = {
                viewModel.updateBookDetails(bookId, status, rating, comment.text)
                onBack()
            }) {
                Text("Guardar cambios")
            }
        }
    }
}

