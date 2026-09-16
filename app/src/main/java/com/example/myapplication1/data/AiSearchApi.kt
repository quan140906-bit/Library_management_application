package com.example.myapplication1.data

import retrofit2.http.GET
import retrofit2.http.Query

interface AiSearchApi {

    @GET("api/ai/search")
    suspend fun searchBooks(
        @Query("q") query: String
    ): List<Book>
}