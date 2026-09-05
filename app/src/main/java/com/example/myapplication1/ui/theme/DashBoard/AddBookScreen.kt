package com.example.myapplication1.ui.theme.DashBoard

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication1.data.Book
import com.example.myapplication1.data.BookRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { BookRepository() }

    // Các biến lưu trữ dữ liệu người dùng nhập vào
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm Sách Mới") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6750A4),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("Tên sách *") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = author, onValueChange = { author = it },
                label = { Text("Tác giả *") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = genre, onValueChange = { genre = it },
                label = { Text("Thể loại") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = isbn, onValueChange = { isbn = it },
                label = { Text("Mã ISBN") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = quantity, onValueChange = { quantity = it },
                label = { Text("Số lượng") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isBlank() || author.isBlank()) {
                        Toast.makeText(context, "Vui lòng nhập tên sách và tác giả!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isSaving = true
                    // Đóng gói dữ liệu thành object Book
                    val newBook = Book(
                        title = title, author = author, genre = genre,
                        isbn = isbn, quantity = quantity.toIntOrNull() ?: 1
                    )
                    // Gọi hàm đẩy lên mạng
                    repository.addBook(
                        book = newBook,
                        onSuccess = {
                            isSaving = false
                            Toast.makeText(context, "Thêm sách thành công!", Toast.LENGTH_SHORT).show()
                            onBackClick() // Tự động quay lại trang chủ
                        },
                        onFailure = { e ->
                            isSaving = false
                            Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !isSaving
            ) {
                Text(if (isSaving) "Đang lưu lên Firebase..." else "Lưu Sách", fontSize = 16.sp)
            }
        }
    }
}