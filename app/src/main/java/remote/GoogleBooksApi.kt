package com.example.bookapp.data.remote

import com.example.bookapp.data.model.GoogleBookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String
    ): GoogleBookResponse
}
