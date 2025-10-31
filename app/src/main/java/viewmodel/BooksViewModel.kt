package com.example.bookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.model.BookItem
import com.example.bookapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BooksViewModel : ViewModel() {

    private val _books = MutableStateFlow<List<BookItem>>(emptyList())
    val books: StateFlow<List<BookItem>> = _books

    fun searchBooks(query: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.api.searchBooks(query)
            _books.value = response.items ?: emptyList()
        }
    }
}
