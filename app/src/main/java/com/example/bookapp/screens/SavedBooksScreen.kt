package com.example.bookapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.bookapp.viewmodel.SavedBooksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedBooksScreen(
    viewModel: SavedBooksViewModel = viewModel(),
    onBookClick: (String) -> Unit
) {
    val books by viewModel.savedBooks.collectAsState()

    // 🔹 Estado del filtro visual
    var selectedFilter by remember { mutableStateOf("Todos") }

    // 🔹 Lista filtrada según el filtro seleccionado
    val filteredBooks = when (selectedFilter) {
        "Leídos" -> books.filter { it.status == "read" }
        "Por leer" -> books.filter { it.status == "unread" || it.status == "to_read" }
        else -> books
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // 🔹 Título
        Text(
            text = "Mis libros guardados",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )

        // 🔹 Filtros visuales
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterButton("Todos", selectedFilter) { selectedFilter = "Todos" }
            FilterButton("Leídos", selectedFilter) { selectedFilter = "Leídos" }

        }

        Divider(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Lista filtrada
        if (filteredBooks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No hay libros en esta categoría.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredBooks) { book ->
                    Card(
                        onClick = { onBookClick(book.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val thumbnail = book.volumeInfo.imageLinks?.thumbnail
                            if (thumbnail != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(thumbnail),
                                    contentDescription = book.volumeInfo.title,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(end = 12.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(Color.LightGray.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("📘", fontSize = 24.sp)
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = book.volumeInfo.title ?: "Sin título",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = book.volumeInfo.authors?.joinToString(", ")
                                        ?: "Autor desconocido",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(Modifier.height(4.dp))
                                val statusLabel = when (book.status) {
                                    "read" -> "Leído"
                                    "unread", "to_read" -> "Por leer"
                                    else -> "Sin estado"
                                }
                                Text(
                                    text = statusLabel,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (book.status == "read")
                                            Color(0xFF2E7D32)
                                        else
                                            MaterialTheme.colorScheme.secondary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 🔹 Botón reutilizable de filtro
@Composable
fun FilterButton(text: String, selected: String, onClick: () -> Unit) {
    val isSelected = text == selected
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
        )
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}
