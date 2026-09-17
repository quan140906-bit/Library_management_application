package com.example.myapplication1.data

data class ReadingProgress(
    val progressId: Long? = null,
    val memberId: Long,
    val bookId: Long,
    val progress: Int = 0,
    val status: String = "UNREAD"
)

data class ProgressUpdateRequest(
    val progress: Int
)