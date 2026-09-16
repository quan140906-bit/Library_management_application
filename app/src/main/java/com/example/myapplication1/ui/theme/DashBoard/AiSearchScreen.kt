package com.example.myapplication1.ui.theme.DashBoard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication1.data.AiSearchRepository
import com.example.myapplication1.data.Book
import kotlinx.coroutines.launch

@Composable
fun AiSearchScreen(
    onBackClick: () -> Unit
) {

    val repository = remember {
        AiSearchRepository()
    }

    val scope = rememberCoroutineScope()

    var query by remember {
        mutableStateOf("")
    }

    var books by remember {
        mutableStateOf<List<Book>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var hasSearched by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "AI Book Search",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Bạn muốn đọc sách gì?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Hãy mô tả bằng ngôn ngữ tự nhiên. AI sẽ hiểu nhu cầu và tìm sách phù hợp trong thư viện.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Nhập yêu cầu")
            },
            placeholder = {
                Text(
                    "Ví dụ: Tôi muốn sách giúp tập trung và làm việc hiệu quả"
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            minLines = 2
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = {

                if (query.isBlank()) {
                    errorMessage = "Hãy nhập nội dung cần tìm."
                    return@Button
                }

                scope.launch {

                    isLoading = true
                    errorMessage = null
                    hasSearched = true

                    try {

                        books = repository.searchBooks(query)

                    } catch (e: Exception) {

                        books = emptyList()

                        errorMessage =
                            e.message
                                ?: "Không thể kết nối tới AI."

                    } finally {

                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )

            Text(
                text = if (isLoading) {
                    " Đang tìm..."
                } else {
                    " Tìm bằng AI"
                }
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (isLoading) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CircularProgressIndicator()

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "AI đang phân tích yêu cầu..."
                )
            }

        } else if (errorMessage != null) {

            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error
            )

        } else if (hasSearched && books.isEmpty()) {

            Text(
                text = "Không tìm thấy sách phù hợp trong thư viện.",
                style = MaterialTheme.typography.bodyLarge
            )

        } else if (books.isNotEmpty()) {

            Text(
                text = "Sách AI đề xuất",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(
                    10.dp
                )
            ) {

                items(
                    items = books,
                    key = { book ->
                        book.bookId ?: book.title
                    }
                ) { book ->

                    AiSearchBookCard(
                        book = book
                    )
                }
            }
        }
    }
}

@Composable
private fun AiSearchBookCard(
    book: Book
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            book.tag
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let { tag ->

                    Text(
                        text = "Chủ đề: $tag",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            book.publishYear?.let { year ->

                Text(
                    text = "Năm xuất bản: $year",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "Còn lại: ${book.availableQuantity ?: 0}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}