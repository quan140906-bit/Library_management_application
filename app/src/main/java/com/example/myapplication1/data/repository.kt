package com.example.myapplication1.data

import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    // Hàm giả lập gọi API đăng ký (Giữ nguyên của bạn)
    fun registerUser(email: String): Boolean {
        return email.isNotEmpty()
    }
}

class BookRepository {
    // --- PHẦN FIREBASE MỚI THÊM VÀO ---
    private val db = FirebaseFirestore.getInstance()
    private val booksCollection = db.collection("books")

    // Hàm 1: Đẩy sách lên Firebase
    fun addBook(book: Book, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val newDocRef = booksCollection.document()
        book.id = newDocRef.id // Lấy ID ngẫu nhiên từ Firebase bằng chữ (String)

        newDocRef.set(book)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // --- XỬ LÝ TẠM CÁC HÀM CŨ ĐỂ APP KHÔNG BỊ LỖI ---

    // Hàm 2: Tạm thời trả về danh sách rỗng (Bài sau mình sẽ viết code kéo sách từ Firebase về đây)
    fun getAllBooks(): List<Book> {
        return emptyList()
    }

    // Hàm 3: Đổi bookId thành String (Do Firebase dùng chữ, không dùng số nguyên Int nữa)
    fun markAsRead(bookId: String) {
        // Tạm thời để trống. Bài sau mình sẽ viết code update trực tiếp lên Firebase
    }
}