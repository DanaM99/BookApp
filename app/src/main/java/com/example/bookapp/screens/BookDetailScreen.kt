package com.example.bookapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    // Cargamos el libro cuando cambia (clave = id para evitar recargas innecesarias)
    LaunchedEffect(book.id) {
        viewModel.loadBook(book)
    }

    val bookState by viewModel.bookState.collectAsState()

    if (bookState == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val ui = bookState!! // atajo local

    val scrollState = rememberScrollState()

    // Estados locales sincronizados con el estado del ViewModel
    var localRating by remember(ui.id) { mutableStateOf(ui.rating.toFloat()) }
    var localComment by remember(ui.id) { mutableStateOf(ui.comment) }

    // Si el estado cambia desde afuera (por ejemplo, repo), actualizamos locales
    LaunchedEffect(ui.rating) { localRating = ui.rating.toFloat() }
    LaunchedEffect(ui.comment) { localComment = ui.comment }

    var savedMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Título
        Text(ui.title, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        // Autores
        Text(ui.authors, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))

        // Portada (si hay)
        ui.thumbnail?.let { thumb ->
            Image(
                painter = rememberAsyncImagePainter(thumb),
                contentDescription = ui.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Spacer(Modifier.height(16.dp))
        }

        // ---- DESCRIPCIÓN (siempre desde el estado del ViewModel) ----
        val description = ui.description ?: "Sin descripción"
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(24.dp))

        // ---------- ESTADO ----------
        Text("Estado:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Row {
            Button(
                onClick = {
                    viewModel.updateField("status", "read")
                    savedMessage = "Estado guardado ✔"
                },
                modifier = Modifier.weight(1f)
            ) { Text("✅ Leído") }

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {
                    viewModel.updateField("status", "to_read")
                    savedMessage = "Estado guardado ✔"
                },
                modifier = Modifier.weight(1f)
            ) { Text("📘 Para leer") }
        }

        Spacer(Modifier.height(24.dp))

        // ---------- RATING ----------
        Text("Puntuación:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Slider(
            value = localRating,
            onValueChange = { localRating = it },
            onValueChangeFinished = {
                viewModel.updateField("rating", localRating.toInt())
                savedMessage = "Puntuación guardada ✔"
            },
            valueRange = 0f..5f,
            steps = 4
        )

        Text("⭐ ${localRating.toInt()} / 5")

        Spacer(Modifier.height(24.dp))

        // ---------- COMENTARIO ----------
        OutlinedTextField(
            value = localComment,
            onValueChange = { localComment = it },
            label = { Text("Comentario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.updateField("comment", localComment)
                savedMessage = "Comentario guardado ✔"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar comentario")
        }

        Spacer(Modifier.height(16.dp))

        savedMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
