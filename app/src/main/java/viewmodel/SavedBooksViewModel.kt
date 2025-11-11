package com.example.bookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.model.BookItem
import com.example.bookapp.data.model.VolumeInfo
import com.example.bookapp.data.model.ImageLinks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SavedBooksViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _savedBooks = MutableStateFlow<List<BookItem>>(emptyList())
    val savedBooks: StateFlow<List<BookItem>> = _savedBooks

    init {
        loadSavedBooks()
    }

    /**
     * Lee los libros guardados del usuario actual desde Firestore.
     */
    fun loadSavedBooks() {
        val user = auth.currentUser ?: return

        viewModelScope.launch {
            firestore.collection("users")
                .document(user.uid)
                .collection("books")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        println("❌ Error al cargar libros: ${error.message}")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val books = snapshot.documents.mapNotNull { doc ->
                            try {
                                BookItem(
                                    id = doc.id,
                                    volumeInfo = VolumeInfo(
                                        title = doc.getString("title"),
                                        authors = doc.get("authors")?.let {
                                            (it as? List<*>)?.filterIsInstance<String>()
                                        },
                                        description = doc.getString("description"),
                                        imageLinks = ImageLinks(
                                            thumbnail = doc.getString("thumbnail")
                                        )
                                    )
                                )
                            } catch (e: Exception) {
                                println("⚠ Error parseando libro: ${e.message}")
                                null
                            }
                        }
                        _savedBooks.value = books
                    } else {
                        _savedBooks.value = emptyList()
                    }
                }
        }
    }
}

