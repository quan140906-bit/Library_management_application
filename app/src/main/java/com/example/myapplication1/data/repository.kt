package com.example.myapplication1.data

class UserRepository {
    // Hàm giả lập gọi API đăng ký
    fun registerUser(email: String): Boolean {
        // Xử lý gửi lên server ở đây...
        return email.isNotEmpty() // Trả về true nếu thành công
    }
}

class BookRepository {    // Test sau này thay, bằng gọi API/Room
    private val books = mutableListOf(
        Book(1, "Đắc Nhân Tâm", "Dale Carnegie", BookStatus.READ),
        Book(2, "Nhà Giả Kim", "Paulo Coelho", BookStatus.READ),
        Book(3, "Atomic Habits", "James Clear", BookStatus.READING),
        Book(4, "Deep Work", "Cal Newport", BookStatus.READING)
    )

    fun getAllBooks(): List<Book> = books.toList()

    /** Đánh dấu 1 sách là đã đọc xong. */
    fun markAsRead(bookId: Int) {
        val index = books.indexOfFirst { it.id == bookId }
        if (index != -1) {
            books[index] = books[index].copy(status = BookStatus.READ)
        }
    }
}