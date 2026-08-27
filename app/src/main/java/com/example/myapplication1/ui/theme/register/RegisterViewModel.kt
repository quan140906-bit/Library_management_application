package com.example.myapplication1.ui.theme.register

import androidx.lifecycle.ViewModel
import com.example.myapplication1.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel : ViewModel() {
    private val repository = UserRepository() // Kết nối với Model

    // Trạng thái giữ giá trị email đang nhập
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    // Hàm được gọi mỗi khi người dùng gõ một chữ cái mới
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    // Hàm xử lý nút Đăng ký
    fun submitRegister() {
        val success = repository.registerUser(_email.value)
        // Xử lý kết quả (ví dụ: báo lỗi hoặc chuyển màn hình)
    }
}