package com.example.myapplication1.data

// =========================
// MEMBER
// =========================

data class Member(
    val memberId: Long? = null,
    val fullName: String,
    val email: String? = null,
    val phone: String? = null,
    val registerDate: String? = null,
    val status: String = "ACTIVE"
)


// =========================
// AUTHOR
// =========================

data class Author(
    val authorId: Long? = null,
    val authorName: String
)


// =========================
// CATEGORY
// =========================

data class Category(
    val categoryId: Long? = null,
    val categoryName: String
)


// =========================
// BOOK STATUS
// =========================

enum class BookStatus {
    READ,
    READING,
    UNREAD
}


// =========================
// BOOK
// =========================

data class Book(
    val bookId: Long? = null,
    val title: String,
    val isbn: String? = null,
    val tag: String? = null,
    val series: String? = null,

    val category: Category? = null,
    val author: Author? = null,

    val quantity: Int = 1,
    val availableQuantity: Int = 1,

    // Tạm dùng cho UI Android
    val status: BookStatus = BookStatus.UNREAD,
    val imageUrl: String? = null
)


// =========================
// BORROW
// =========================

data class Borrow(
    val borrowId: Long? = null,
    val member: Member? = null,
    val borrowDate: String? = null,
    val dueDate: String,
    val status: String = "BORROWING"
)


// =========================
// BORROW DETAIL
// =========================

data class BorrowDetail(
    val borrowId: Long,
    val bookId: Long,
    val borrow: Borrow? = null,
    val book: Book? = null,
    val returnDate: String? = null
)