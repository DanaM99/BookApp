package com.example.bookapp.data.model

// Respuesta general de la API de Google Books
data class GoogleBookResponse(
    val items: List<BookItem>?
)

// Modelo principal de libro
data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo,
    val status: String? = "none",   // Estado del libro (leído / no leído)
    val rating: Int? = 0,           // Puntuación del usuario
    val comment: String? = ""       // Reseña o comentario
)

// Información detallada del libro
data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val pageCount: Int? = null
)

// Imagen del libro
data class ImageLinks(
    val thumbnail: String?
)

