package com.example.bookapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookapp.data.model.BookItem
import com.example.bookapp.viewmodel.BookDetailViewModel

@Composable
fun BookDetailScreen(
    book: BookItem,
    viewModel: BookDetailViewModel = viewModel()
) {
    var savedMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 📘 Título
        Text(book.volumeInfo.title ?: "Sin título", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        // ✍️ Autores
        book.volumeInfo.authors?.let {
            Text(it.joinToString(), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
        }

        // 🖼 Imagen del libro (se asegura HTTPS)
        book.volumeInfo.imageLinks?.thumbnail?.let { thumb ->
            Image(
                painter = rememberAsyncImagePainter(thumb.replace("http://", "https://")),
                contentDescription = book.volumeInfo.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Spacer(Modifier.height(16.dp))
        }

        // 📝 Descripción
        Text(
            text = book.volumeInfo.description ?: "Sin descripción disponible.",
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(24.dp))

        // 💾 Botón para guardar libro
        Button(
            onClick = {
                viewModel.saveBook(book) { success ->
                    savedMessage = if (success)
                        "Libro guardado ✔"
                    else
                        "El libro ya existe en su biblioteca"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar libro 📚")
        }

        Spacer(Modifier.height(16.dp))

        // ✅ Mensaje de estado
        savedMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

