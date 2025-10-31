package com.example.bookapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.bookapp.data.model.BookItem
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun BookDetailScreen(book: BookItem) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = book.volumeInfo.title ?: "Sin título",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.volumeInfo.authors?.joinToString() ?: "Autor desconocido",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = rememberAsyncImagePainter(book.volumeInfo.imageLinks?.thumbnail),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = book.volumeInfo.description ?: "Sin descripción",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val db = FirebaseFirestore.getInstance()
                db.collection("favorites").add(book)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("❤️ Agregar a favoritos")
        }
    }
}
