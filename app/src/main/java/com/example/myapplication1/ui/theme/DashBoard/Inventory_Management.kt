package com.example.myapplication1.ui.theme.DashBoard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication1.data.Book
import com.example.myapplication1.data.BookRepository
import com.example.myapplication1.data.BookStatus
import kotlinx.coroutines.launch

val PrimaryPurple = Color(0xFF6750A4)

private enum class BookFilter {
    ALL, READ, UNREAD
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryManagementScreen() {
    val repository = remember { BookRepository() }
    var books by remember { mutableStateOf(repository.getAllBooks()) }

    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(BookFilter.ALL) }
    var selectedBookId by remember { mutableStateOf<Int?>(null) }
    var showAddBookScreen by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Màn hình Thêm sách hiển thị riêng, thay cho toàn bộ nội dung bên dưới
    if (showAddBookScreen) {
        AddBookScreen(
            onCancel = { showAddBookScreen = false },
            onSave = { title, author, imageUrl, publishYear, genre ->
                repository.addBook(title, author, imageUrl, publishYear, genre)
                books = repository.getAllBooks()
                showAddBookScreen = false
            }
        )
        return
    }

    val selectedBook = books.firstOrNull { it.id == selectedBookId }

    val total = books.size
    val readCount = books.count { it.status == BookStatus.READ }
    val readingCount = books.count { it.status == BookStatus.READING }
    val unreadCount = books.count { it.status == BookStatus.UNREAD }
    val progressPercent = if (total == 0) 0 else (readCount * 100) / total

    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent / 100f,
        animationSpec = tween(durationMillis = 450),
        label = "progressAnim"
    )

    val filteredBooks = books
        .filter { book ->
            when (selectedFilter) {
                BookFilter.ALL -> true
                BookFilter.READ -> book.status == BookStatus.READ
                BookFilter.UNREAD -> book.status == BookStatus.UNREAD || book.status == BookStatus.READING
            }
        }
        .filter { book ->
            searchQuery.isBlank() ||
                    book.title.contains(searchQuery, ignoreCase = true) ||
                    book.author.contains(searchQuery, ignoreCase = true)
        }

    val sectionTitle = when (selectedFilter) {
        BookFilter.ALL -> "Tất cả sách"
        BookFilter.READ -> "Sách đã đọc"
        BookFilter.UNREAD -> "Sách chưa đọc"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "My Library",
                    style = MaterialTheme.typography.titleLarge,
                    color = PrimaryPurple,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                NavigationDrawerItem(
                    label = { Text("Trang chủ") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    selected = true,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Thêm sách") },
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = null) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        showAddBookScreen = true
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Tài khoản") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        // TODO: điều hướng sang màn hình Tài khoản sau này
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Cài đặt") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        // TODO: điều hướng sang màn hình Cài đặt sau này
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (isSearching) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Tìm sách theo tên hoặc tác giả", color = Color.White.copy(alpha = 0.7f)) },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedTextColor = Color.White,
                                    focusedTextColor = Color.White,
                                    cursorColor = Color.White
                                )
                            )
                        } else {
                            Text("My Library ALFM")
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            if (isSearching) searchQuery = ""
                            isSearching = !isSearching
                        }) {
                            Icon(
                                imageVector = if (isSearching) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = if (isSearching) "Đóng tìm kiếm" else "Tìm kiếm",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryPurple,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Text("Quản lý tủ sách của bạn", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    StatCard(label = "Tổng sách", value = total.toString(), modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(12.dp))
                    StatCard(label = "Đã đọc", value = readCount.toString(), modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    StatCard(label = "Đang đọc", value = readingCount.toString(), modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(12.dp))
                    StatCard(label = "Chưa đọc", value = unreadCount.toString(), modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(28.dp))

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

                Text(sectionTitle, style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedFilter == BookFilter.ALL,
                        onClick = { selectedFilter = BookFilter.ALL },
                        label = { Text("Tất cả sách") }
                    )
                    FilterChip(
                        selected = selectedFilter == BookFilter.READ,
                        onClick = { selectedFilter = BookFilter.READ },
                        label = { Text("Sách đã đọc") }
                    )
                    FilterChip(
                        selected = selectedFilter == BookFilter.UNREAD,
                        onClick = { selectedFilter = BookFilter.UNREAD },
                        label = { Text("Sách chưa đọc") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn {
                    items(filteredBooks, key = { it.id }) { book ->
                        BookReadingItem(
                            book = book,
                            onClick = { selectedBookId = book.id },
                            onMarkRead = {
                                repository.markAsRead(book.id)
                                books = repository.getAllBooks()
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        selectedBook?.let { book ->
            AlertDialog(
                onDismissRequest = { selectedBookId = null },
                title = { Text(book.title) },
                text = {
                    Column {
                        Text("Tác giả: ${book.author}")
                        Spacer(modifier = Modifier.height(10.dp))
                        StatusBadge(status = book.status)
                    }
                },
                confirmButton = {
                    if (book.status != BookStatus.READ) {
                        TextButton(onClick = {
                            repository.markAsRead(book.id)
                            books = repository.getAllBooks()
                        }) {
                            Text("Xác nhận đã đọc xong")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedBookId = null }) {
                        Text("Đóng")
                    }
                }
            )
        }
    }
}

@Composable
private fun StatusBadge(status: BookStatus) {
    val (label, color) = when (status) {
        BookStatus.READ -> "Đã đọc" to Color(0xFF2FA84F)
        BookStatus.READING -> "Đang đọc" to PrimaryPurple
        BookStatus.UNREAD -> "Chưa đọc" to Color(0xFF9AA0A6)
    }

    Box(
        modifier = Modifier
            .background(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "Trạng thái: $label",
            color = color,
            style = MaterialTheme.typography.labelLarge
        )
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
private fun BookReadingItem(book: Book, onClick: () -> Unit, onMarkRead: () -> Unit) {
    Card(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
            if (book.status == BookStatus.READING) {
                Button(
                    onClick = onMarkRead,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("Đã đọc xong")
                }
            }
        }
    }
}