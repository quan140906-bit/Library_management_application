package com.example.myapplication1.ui.theme.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen(
    // Khởi tạo ViewModel tự động cho Compose
    viewModel: RegisterViewModel = viewModel()
) {
    // Lắng nghe biến email từ ViewModel
    val email by viewModel.email.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(13.dp))

        // Ô NHẬP EMAIL
        OutlinedTextField(
            value = email, // Đọc giá trị từ ViewModel
            onValueChange = { newText ->
                viewModel.onEmailChange(newText) // Báo cho ViewModel khi có thay đổi
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = 15.dp),
            label = { Text("Email") },
            placeholder = { Text("abcxyz@gmail.com") }
        )

        Spacer(modifier = Modifier.height(20.dp)) // Tạo khoảng trống 20dp cho đẹp

        // NÚT ĐĂNG KÝ (ĐÃ ĐƯỢC NỐI DÂY ĐIỆN) 🔌
        Button(
            onClick = {
                // Khi người dùng bấm nút, lệnh này sẽ gọi Firebase ra làm việc!
                viewModel.submitRegister()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = 15.dp)
        ) {
            Text("Đăng ký ngay")
        }
    }
}