package com.example.myapplication1.data

enum class BookStatus {
    READ,
    READING,
    UNREAD
}

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    var status: BookStatus,
    val imageUrl: String? = null,
    val publishYear: Int? = null,
    val genre: String? = null
)