package com.example.myapplication1.data

// Giữ lại Enum của bạn
enum class BookStatus {
    READ,
    READING,
    UNREAD
}

data class Book(
    var id: String = "",            // ID kiểu String cho Firebase
    var title: String = "",
    var author: String = "",
    var genre: String = "",
    var isbn: String = "",
    var quantity: Int = 0,
    var coverUrl: String = "",
    var status: BookStatus = BookStatus.UNREAD // Khôi phục lại trạng thái
)