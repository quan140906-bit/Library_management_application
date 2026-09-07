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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication1.data.Book
import com.example.myapplication1.data.BookStatus
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
val PrimaryPurple = Color(0xFF6750A4)


private enum class BookFilter {
    ALL, READ, UNREAD
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryManagementScreen(
    books: List<Book>,
    onMarkAsRead: (Int) -> Unit,
    onNavigateToAddBook: () -> Unit
) {

    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(BookFilter.ALL) }
    var selectedBookId by remember { mutableStateOf<Int?>(null) }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    val selectedBook = books.firstOrNull {
        it.id == selectedBookId
    }

    // =========================
    // THỐNG KÊ
    // =========================

    val total = books.size

    val readCount = books.count {
        it.status == BookStatus.READ
    }

    val readingCount = books.count {
        it.status == BookStatus.READING
    }

    val unreadCount = books.count {
        it.status == BookStatus.UNREAD
    }

    val progressPercent =
        if (total == 0) 0
        else (readCount * 100) / total

    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent / 100f,
        animationSpec = tween(500),
        label = "libraryProgress"
    )

    // =========================
    // LỌC + TÌM KIẾM
    // =========================

    val filteredBooks = books
        .filter { book ->

            when (selectedFilter) {

                BookFilter.ALL -> true

                BookFilter.READ ->
                    book.status == BookStatus.READ

                BookFilter.UNREAD ->
                    book.status == BookStatus.UNREAD ||
                            book.status == BookStatus.READING
            }
        }
        .filter { book ->

            searchQuery.isBlank() ||
                    book.title.contains(
                        searchQuery,
                        ignoreCase = true
                    ) ||
                    book.author.contains(
                        searchQuery,
                        ignoreCase = true
                    )
        }

    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                Text(
                    text = "ALFM",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple,
                    modifier = Modifier.padding(
                        horizontal = 24.dp
                    )
                )

                Text(
                    text = "Library Management",
                    color = Color.Gray,
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 4.dp
                    )
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                NavigationDrawerItem(
                    label = {
                        Text("Trang chủ")
                    },
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null
                        )
                    },
                    selected = true,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    modifier = Modifier.padding(
                        horizontal = 12.dp
                    )
                )

                NavigationDrawerItem(
                    label = {
                        Text("Thêm sách")
                    },
                    icon = {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = null
                        )
                    },
                    selected = false,
                    onClick = {

                        scope.launch {
                            drawerState.close()
                        }

                        onNavigateToAddBook()
                    },
                    modifier = Modifier.padding(
                        horizontal = 12.dp
                    )
                )

                NavigationDrawerItem(
                    label = {
                        Text("Tài khoản")
                    },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    modifier = Modifier.padding(
                        horizontal = 12.dp
                    )
                )

                NavigationDrawerItem(
                    label = {
                        Text("Cài đặt")
                    },
                    icon = {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = null
                        )
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    modifier = Modifier.padding(
                        horizontal = 12.dp
                    )
                )
            }
        }
    ) {

        Scaffold(

            containerColor = Color(0xFFF8F6FC),

            topBar = {

                TopAppBar(

                    title = {

                        if (isSearching) {

                            TextField(
                                value = searchQuery,
                                onValueChange = {
                                    searchQuery = it
                                },
                                placeholder = {
                                    Text(
                                        "Tìm tên sách hoặc tác giả",
                                        color = Color.White.copy(
                                            alpha = 0.7f
                                        )
                                    )
                                },
                                singleLine = true,

                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor =
                                        Color.Transparent,
                                    unfocusedContainerColor =
                                        Color.Transparent,
                                    focusedIndicatorColor =
                                        Color.Transparent,
                                    unfocusedIndicatorColor =
                                        Color.Transparent,
                                    focusedTextColor =
                                        Color.White,
                                    unfocusedTextColor =
                                        Color.White,
                                    cursorColor =
                                        Color.White
                                )
                            )

                        } else {

                            Column {

                                Text(
                                    text = "Trang chủ",
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Quản lý thư viện",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(
                                        alpha = 0.8f
                                    )
                                )
                            }
                        }
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {

                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },

                    actions = {

                        IconButton(
                            onClick = {

                                if (isSearching) {
                                    searchQuery = ""
                                }

                                isSearching = !isSearching
                            }
                        ) {

                            Icon(
                                imageVector =
                                    if (isSearching)
                                        Icons.Default.Close
                                    else
                                        Icons.Default.Search,

                                contentDescription = "Tìm kiếm",

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
            },

            floatingActionButton = {

                FloatingActionButton(

                    onClick = onNavigateToAddBook,

                    containerColor = PrimaryPurple,
                    contentColor = Color.White,

                    shape = RoundedCornerShape(18.dp)
                ) {

                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Thêm sách"
                    )
                }
            }

        ) { innerPadding ->

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 18.dp)

            ) {

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "Xin chào 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Quản lý tủ sách của bạn",
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // =========================
                // THỐNG KÊ
                // =========================

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    StatCard(
                        label = "Tổng sách",
                        value = total.toString(),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    StatCard(
                        label = "Đã đọc",
                        value = readCount.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    StatCard(
                        label = "Đang đọc",
                        value = readingCount.toString(),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    StatCard(
                        label = "Chưa đọc",
                        value = unreadCount.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                // =========================
                // TIẾN ĐỘ
                // =========================

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {

                            Text(
                                "Tiến độ đọc",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "$progressPercent%",
                                color = PrimaryPurple,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        LinearProgressIndicator(
                            progress = {
                                animatedProgress
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(
                                    RoundedCornerShape(20.dp)
                                ),

                            color = PrimaryPurple,
                            trackColor = Color(0xFFE8E2F1)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // =========================
                // DANH SÁCH
                // =========================

                Text(
                    text = "Sách của tôi",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(7.dp)
                ) {

                    FilterChip(
                        selected =
                            selectedFilter == BookFilter.ALL,
                        onClick = {
                            selectedFilter = BookFilter.ALL
                        },
                        label = {
                            Text("Tất cả")
                        }
                    )

                    FilterChip(
                        selected =
                            selectedFilter == BookFilter.READ,
                        onClick = {
                            selectedFilter = BookFilter.READ
                        },
                        label = {
                            Text("Đã đọc")
                        }
                    )

                    FilterChip(
                        selected =
                            selectedFilter == BookFilter.UNREAD,
                        onClick = {
                            selectedFilter = BookFilter.UNREAD
                        },
                        label = {
                            Text("Chưa đọc")
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                if (filteredBooks.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "📚",
                                style =
                                    MaterialTheme.typography.displaySmall
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = "Không tìm thấy sách",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Thử tìm kiếm với từ khóa khác",
                                color = Color.Gray
                            )
                        }
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),

                        contentPadding = PaddingValues(
                            top = 4.dp,
                            bottom = 90.dp
                        ),

                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        items(
                            filteredBooks,
                            key = {
                                it.id
                            }
                        ) { book ->

                            BookReadingItem(
                                book = book,

                                onClick = {
                                    selectedBookId = book.id
                                },

                                onMarkRead = {
                                    onMarkAsRead(book.id)
                                }
                            )
                        }
                    }
                }
            }
        }

        // =========================
        // CHI TIẾT SÁCH
        // =========================

        selectedBook?.let { book ->

            AlertDialog(

                onDismissRequest = {
                    selectedBookId = null
                },

                shape = RoundedCornerShape(24.dp),

                title = {

                    Text(
                        text = book.title,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )
                },

                text = {

                    Column {

                        Text(
                            text = "Tác giả",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = book.author,
                            color = Color.Gray
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        StatusBadge(
                            status = book.status
                        )
                    }
                },

                confirmButton = {

                    if (book.status != BookStatus.READ) {

                        Button(
                            onClick = {

                                onMarkAsRead(book.id)
                                selectedBookId = null
                            },

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        PrimaryPurple
                                )
                        ) {

                            Text("Đã đọc xong")
                        }
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {
                            selectedBookId = null
                        }
                    ) {

                        Text("Đóng")
                    }
                }
            )
        }
    }
}

// ======================================================
// STAT CARD
// ======================================================

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Card(

        modifier = modifier.height(92.dp),

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {
            val icon = when (label) {
                "Tổng sách" -> Icons.Default.MenuBook
                "Đã đọc" -> Icons.Default.CheckCircle
                "Đang đọc" -> Icons.Default.AutoStories
                "Chưa đọc" -> Icons.Default.Bookmark
                else -> Icons.Default.MenuBook
            }

            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = PrimaryPurple,
                modifier = Modifier.size(28.dp)
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )
            Text(
                text = value,
                style =
                    MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryPurple
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = label,
                style =
                    MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

// ======================================================
// STATUS
// ======================================================

@Composable
private fun StatusBadge(
    status: BookStatus
) {

    val (label, color) = when (status) {

        BookStatus.READ ->
            "Đã đọc" to Color(0xFF2FA84F)

        BookStatus.READING ->
            "Đang đọc" to PrimaryPurple

        BookStatus.UNREAD ->
            "Chưa đọc" to Color(0xFF9AA0A6)
    }

    Box(

        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.12f),
                shape = RoundedCornerShape(50)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 7.dp
            )
    ) {

        Text(
            text = label,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

// ======================================================
// BOOK ITEM
// ======================================================

@Composable
private fun BookReadingItem(
    book: Book,
    onClick: () -> Unit,
    onMarkRead: () -> Unit
) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // =========================
            // BOOK COVER PLACEHOLDER
            // =========================

            if (book.imageUrl != null && book.imageUrl.isNotBlank()) {

                coil.compose.AsyncImage(
                    model = book.imageUrl,
                    contentDescription = book.title,
                    modifier = Modifier
                        .size(
                            width = 58.dp,
                            height = 76.dp
                        )
                        .clip(
                            RoundedCornerShape(10.dp)
                        ),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )

            } else {

                Box(
                    modifier = Modifier
                        .size(
                            width = 58.dp,
                            height = 76.dp
                        )
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                        .background(
                            Color(0xFFEDE7F6)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "📖",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )

                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }

            if (book.status != BookStatus.READ) {
                IconButton(onClick = onMarkRead) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Đã đọc",
                        tint = PrimaryPurple
                    )
                }
            }
        }
    }
}