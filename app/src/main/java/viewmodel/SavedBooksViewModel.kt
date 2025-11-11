package com.example.bookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.model.BookItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.bookapp.data.model.VolumeInfo
import com.example.bookapp.data.model.ImageLinks

class SavedBooksViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _savedBooks = MutableStateFlow<List<BookItem>>(emptyList())
    val savedBooks: StateFlow<List<BookItem>> = _savedBooks

    init {
        loadSavedBooks()
    }

    fun loadSavedBooks() {
        val user = auth.currentUser ?: return

        firestore.collection("users")
            .document(user.uid)
            .collection("books")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val books = snapshot.documents.mapNotNull { doc ->
                    val id = doc.id
                    val title = doc.getString("title")
                    val authors = doc.get("authors") as? List<String>
                    val description = doc.getString("description")
                    val thumbnail = doc.getString("thumbnail")
                    val pageCount = doc.getLong("pageCount")?.toInt()

                    if (title != null) {
                        BookItem(
                            id = id,
                            volumeInfo = VolumeInfo(
                                title = title,
                                authors = authors,
                                description = description,
                                imageLinks = ImageLinks(thumbnail),
                                pageCount = pageCount
                            )
                        )
                    } else null
                }
                _savedBooks.value = books
            }
    }


    fun getBookById(bookId: String) = savedBooks.map { list ->
        list.find { it.id == bookId }
    }

    fun deleteBook(bookId: String) {
        val user = auth.currentUser ?: return
        firestore.collection("users")
            .document(user.uid)
            .collection("books")
            .document(bookId)
            .delete()
            .addOnSuccessListener {
                _savedBooks.value = _savedBooks.value.filterNot { it.id == bookId }
            }
    }
}
