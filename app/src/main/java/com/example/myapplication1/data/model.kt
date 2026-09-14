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
    val quantity: Int,
    val availableQuantity: Int
)


// =========================
// BORROW
// =========================

data class Borrow(
    val borrowId: Long? = null,
    val member: Member? = null,
    val borrowDate: String? = null,
    val dueDate: String,
    val status: String = "BORROWED"
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