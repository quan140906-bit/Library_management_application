package com.example.myapplication1.data

class UserRepository {
    fun registerUser(email: String): Boolean {
        return email.isNotEmpty()
    }
}

class BookRepository {
    private val books = mutableListOf(
        Book(1, "Đắc Nhân Tâm", "Dale Carnegie", BookStatus.READ),
        Book(2, "Nhà Giả Kim", "Paulo Coelho", BookStatus.READ),
        Book(3, "Atomic Habits", "James Clear", BookStatus.READING),
        Book(4, "Deep Work", "Cal Newport", BookStatus.READING)
    )

    fun getAllBooks(): List<Book> = books.toList()

    // Đánh dấu 1 sách là đã đọc xong.
    fun markAsRead(bookId: Int) {
        val index = books.indexOfFirst { it.id == bookId }
        if (index != -1) {
            books[index] = books[index].copy(status = BookStatus.READ)
        }
    }

    // Thêm 1 sách mới, tự sinh id kế tiếp.
    fun addBook(
        title: String,
        author: String,
        imageUrl: String?,
        publishYear: Int?,
        genre: String?
    ) {
        val newId = (books.maxOfOrNull { it.id } ?: 0) + 1
        books.add(
            Book(
                id = newId,
                title = title,
                author = author,
                status = BookStatus.UNREAD,
                imageUrl = imageUrl,
                publishYear = publishYear,
                genre = genre
            )
        )
    }
}