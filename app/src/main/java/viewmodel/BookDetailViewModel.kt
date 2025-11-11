package com.example.bookapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bookapp.data.model.BookItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    val status: String = "to_read"
)

class BookDetailViewModel : ViewModel() {

    // ✅ AGREGAMOS FIREBASE
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _bookState = MutableStateFlow<BookDetailUi?>(null)
    val bookState: StateFlow<BookDetailUi?> = _bookState.asStateFlow()

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

    fun updateField(field: String, value: Any) {
        val current = _bookState.value ?: return
        when (field) {
            "status" -> _bookState.value = current.copy(status = value as String)
            "rating" -> _bookState.value = current.copy(rating = (value as Number).toInt())
            "comment" -> _bookState.value = current.copy(comment = value as String)
            "description" -> _bookState.value = current.copy(description = value as String)
        }
    }

    // ✅ NUEVO: función para guardar el libro en Firestore
    fun saveBook(book: BookItem, onComplete: (Boolean) -> Unit) {
        val user = auth.currentUser ?: return onComplete(false)

        val data = hashMapOf(
            "title" to book.volumeInfo.title,
            "authors" to book.volumeInfo.authors,
            "description" to book.volumeInfo.description,
            "thumbnail" to book.volumeInfo.imageLinks?.thumbnail
        )

        firestore.collection("users")
            .document(user.uid)
            .collection("books")
            .document(book.id ?: book.volumeInfo.title ?: "sin_id")
            .set(data)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
