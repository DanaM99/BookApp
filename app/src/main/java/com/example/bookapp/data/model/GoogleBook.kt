package com.example.bookapp.data.model

data class GoogleBookResponse(
    val items: List<BookItem>?
)

data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val pageCount: Int? = null
)

data class ImageLinks(
    val thumbnail: String?
)
