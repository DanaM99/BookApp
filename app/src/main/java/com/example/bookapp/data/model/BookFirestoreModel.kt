package com.example.bookapp.data.model

data class BookFirestoreModel(
    val userId: String = "",
    val bookId: String = "",
    val title: String = "",
    val authors: String = "",
    val thumbnail: String = "",
    val description: String = "",
    val status: String = "none",
    val rating: Int = 0,
    val comment: String = ""
)
