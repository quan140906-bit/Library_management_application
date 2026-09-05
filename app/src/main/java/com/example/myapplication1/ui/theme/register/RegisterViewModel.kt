package com.example.myapplication1.ui.theme.register

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
// 2 dòng này dùng để gọi Firebase vào làm việc
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RegisterViewModel : ViewModel() {

    // Trạng thái giữ giá trị email
    private val _email = MutableStateFlow(value = "")
    val email: StateFlow<String> = _email.asStateFlow()

    // Hàm cập nhật email mỗi khi người dùng gõ phím
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    // Hàm này sẽ chạy khi người dùng bấm nút "Đăng ký"
    fun submitRegister() {
        // 1. Gọi cái kho Firestore trên mạng mà bạn vừa tạo ra
        val db = Firebase.firestore

        // 2. Tạo một "gói hàng" chứa email mà người dùng vừa nhập
        val thongTinNguoiDung = hashMapOf(
            "email_dang_ky" to _email.value,
            "thoi_gian" to "Vừa mới đăng ký xong"
        )

        // 3. Quăng "gói hàng" đó vào một cái kệ tên là "Danh_sach_User" trên mạng
        db.collection("Danh_sach_User")
            .add(thongTinNguoiDung)
            .addOnSuccessListener {
                // Nếu quăng lên mạng thành công, nó sẽ báo dòng này
                Log.d("KiemTra", "Tuyệt vời! Đã lưu email lên mạng thành công!")
            }
            .addOnFailureListener { e ->
                // Nếu mạng lỗi, nó báo dòng này
                Log.w("KiemTra", "Ối, lỗi rồi: ", e)
            }
    }
}