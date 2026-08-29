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

    val total = books.size
    val readCount = books.count { it.status == BookStatus.READ }
    val readingCount = books.count { it.status == BookStatus.READING }
    val unreadCount = books.count { it.status == BookStatus.UNREAD }
    val progressPercent = if (total == 0) 0 else (readCount * 100) / total

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

        // Hàng 1: Tổng sách + Đã đọc
        Row(modifier = Modifier.fillMaxWidth()) {
            StatCard(label = "Tổng sách", value = total.toString(), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(12.dp))
            StatCard(label = "Đã đọc", value = readCount.toString(), modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Hàng 2: Đang đọc + Chưa đọc
        Row(modifier = Modifier.fillMaxWidth()) {
            StatCard(label = "Đang đọc", value = readingCount.toString(), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(12.dp))
            StatCard(label = "Chưa đọc", value = unreadCount.toString(), modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(28.dp))

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
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = modifier.height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall)
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