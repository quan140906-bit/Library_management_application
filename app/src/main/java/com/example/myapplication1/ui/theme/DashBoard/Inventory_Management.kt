package com.example.myapplication1.ui.theme.DashBoard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.myapplication1.data.Book
import com.example.myapplication1.data.BookRepository
import com.example.myapplication1.data.BookStatus

@Composable
fun InventoryManagementScreen() {
    val repository = remember { BookRepository() }
    var books by remember { mutableStateOf(repository.getAllBooks()) }

    val readCount = books.count { it.status == BookStatus.READ }
    val progressPercent = if (books.isEmpty()) 0 else (readCount * 100) / books.size

    // Thanh tiến độ tự động animate mượt mỗi khi progressPercent thay đổi
    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent / 100f,
        animationSpec = tween(durationMillis = 450),
        label = "progressAnim"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("My Library", style = MaterialTheme.typography.headlineMedium)
        Text("Quản lý tủ sách của bạn", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // Thẻ tiến độ
        Card(
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tiến độ tủ sách", style = MaterialTheme.typography.titleMedium)
                    Text("$progressPercent%", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
        Text("Sách đang đọc", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(books.filter { it.status == BookStatus.READING }, key = { it.id }) { book ->
                BookReadingItem(
                    book = book,
                    onMarkRead = {
                        repository.markAsRead(book.id)
                        books = repository.getAllBooks() // trigger recompose -> progress bar tự chạy
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun BookReadingItem(book: Book, onMarkRead: () -> Unit) {
    Card(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(book.title, style = MaterialTheme.typography.titleSmall)
                Text(book.author, style = MaterialTheme.typography.bodySmall)
            }
            Button(onClick = onMarkRead) {
                Text("Đã đọc xong")
            }
        }
    }
}