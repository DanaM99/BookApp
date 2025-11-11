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

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _bookState = MutableStateFlow<BookDetailUi?>(null)
    val bookState: StateFlow<BookDetailUi?> = _bookState.asStateFlow()

    fun loadBook(book: BookItem) {
        val title = book.volumeInfo.title ?: "Sin título"
        val authors = book.volumeInfo.authors?.joinToString(", ") ?: "Autor desconocido"
        val thumb = book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
        val description = book.volumeInfo.description

        _bookState.value = BookDetailUi(
            id = book.id,
            title = title,
            authors = authors,
            thumbnail = thumb,
            description = description
        )
    }

    fun saveBook(book: BookItem, onComplete: (Boolean) -> Unit) {
        val user = auth.currentUser ?: return onComplete(false)
        val userBooksRef = firestore.collection("users")
            .document(user.uid)
            .collection("books")

        val bookId = book.id.ifEmpty { book.volumeInfo.title ?: "sin_id" }

        userBooksRef.document(bookId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    onComplete(false) // Ya existe
                } else {
                    // ⚡ Nos aseguramos de que si no tiene pageCount, guardamos 0 en su lugar
                    val data = hashMapOf(
                        "id" to book.id,
                        "title" to (book.volumeInfo.title ?: "Sin título"),
                        "authors" to (book.volumeInfo.authors ?: emptyList<String>()),
                        "description" to (book.volumeInfo.description ?: ""),
                        "thumbnail" to (book.volumeInfo.imageLinks?.thumbnail ?: ""),
                        "pageCount" to (book.volumeInfo.pageCount ?: 0)
                    )

                    userBooksRef.document(bookId).set(data)
                        .addOnSuccessListener { onComplete(true) }
                        .addOnFailureListener { onComplete(false) }
                }
            }
            .addOnFailureListener { onComplete(false) }
    }
}
