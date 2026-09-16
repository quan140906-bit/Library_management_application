package com.example.myapplication1.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AiSearchRepository {

    private val api: AiSearchApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8082/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(AiSearchApi::class.java)
    }

    suspend fun searchBooks(query: String): List<Book> {
        if (query.isBlank()) {
            return emptyList()
        }

        return api.searchBooks(query.trim())
    }
}