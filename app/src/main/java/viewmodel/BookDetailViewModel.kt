package com.example.bookapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bookapp.data.model.BookItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BookDetailUi(
    val id: String,
    val title: String,
    val authors: String,
    val thumbnail: String?,
    val description: String?,
    val rating: Int = 0,
    val comment: String = "",
    val status: String = "to_read" // "read" | "to_read"
)

class BookDetailViewModel : ViewModel() {

    private val _bookState = MutableStateFlow<BookDetailUi?>(null)
    val bookState: StateFlow<BookDetailUi?> = _bookState.asStateFlow()

    /**
     * Carga el libro seleccionado en el estado de UI.
     * La descripción se toma de la API (book.volumeInfo.description).
     */
    fun loadBook(book: BookItem) {
        val title = book.volumeInfo.title ?: "Sin título"
        val authors = book.volumeInfo.authors?.joinToString(", ") ?: "Autor desconocido"
        val thumb = book.volumeInfo.imageLinks?.thumbnail
            ?.replace("http://", "https://")
        val description = book.volumeInfo.description

        _bookState.value = BookDetailUi(
            id = book.id,
            title = title,
            authors = authors,
            thumbnail = thumb,
            description = description
        )
    }

    /**
     * Mantengo tu API `updateField(campo, valor)` para no tocar otras pantallas.
     * Si preferís, luego lo migramos a métodos tipados (updateStatus, updateRating, etc.).
     */
    fun updateField(field: String, value: Any) {
        val current = _bookState.value ?: return
        when (field) {
            "status" -> _bookState.value = current.copy(status = value as String)
            "rating" -> _bookState.value = current.copy(rating = (value as Number).toInt())
            "comment" -> _bookState.value = current.copy(comment = value as String)
            "description" -> _bookState.value = current.copy(description = value as String)
        }
        // TODO: acá podés persistir en Room/Firestore/tu repositorio si querés
    }
}
