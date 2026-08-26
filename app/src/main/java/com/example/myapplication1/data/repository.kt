package com.example.myapplication1.data

class UserRepository {
    // Hàm giả lập gọi API đăng ký
    fun registerUser(email: String): Boolean {
        // Xử lý gửi lên server ở đây...
        return email.isNotEmpty() // Trả về true nếu thành công
    }
}