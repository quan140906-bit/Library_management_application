package com.example.myapplication1.ui.theme.DashBoard

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication1.data.Book
import com.example.myapplication1.data.BookStatus
import com.example.myapplication1.data.MemberRepository
import com.example.myapplication1.data.ReadingProgressRepository
import kotlinx.coroutines.launch


// ======================================================
// COLORS
// ======================================================

val PrimaryPurple = Color(0xFF7357D8)

private val DeepPurple = Color(0xFF4E3AA8)
private val SoftPurple = Color(0xFFEDE8FF)
private val VerySoftPurple = Color(0xFFF7F4FF)

private val AppBackground = Color(0xFFF7F7FB)
private val SurfaceWhite = Color(0xFFFFFFFF)

private val TextPrimary = Color(0xFF20202A)
private val TextSecondary = Color(0xFF777783)

private val SuccessGreen = Color(0xFF2DA66A)
private val WarningOrange = Color(0xFFF39A3C)

private val SoftGreen = Color(0xFFE8F7EF)
private val SoftOrange = Color(0xFFFFF1E3)


// ======================================================
// ENUMS
// ======================================================

private enum class DashboardPage {
    HOME,
    ACCOUNT,
    SETTINGS
}

private enum class BookFilter {
    ALL,
    READING,
    READ,
    UNREAD
}


// ======================================================
// MAIN DASHBOARD
// ======================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryManagementScreen(
    books: List<Book>,
    onMarkAsRead: (Int) -> Unit,
    onNavigateToAddBook: () -> Unit,
    onNavigateToAiSearch: () -> Unit,
    memberId: Long
) {
    var currentPage by remember { mutableStateOf(DashboardPage.HOME) }
    var searchMode by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(BookFilter.ALL) }
    var selectedBookId by remember { mutableStateOf<Long?>(null) }

    val viewCounts = remember { mutableStateMapOf<Long, Int>() }
    val progressByBook = remember { mutableStateMapOf<Long, Int>() }
    val readingProgressRepository = remember { ReadingProgressRepository() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(books) {
        books.forEach { book ->
            val id = book.bookId
            if (!viewCounts.containsKey(id)) {
                viewCounts[id] = 0
            }
        }
    }

    // Load trạng thái/tiến độ thật của user từ Oracle thông qua Spring Boot.
    LaunchedEffect(memberId, books) {
        try {
            val rows = readingProgressRepository.getMemberProgress(memberId)
            progressByBook.clear()
            rows.forEach { row ->
                progressByBook[row.bookId] = row.progress.coerceIn(0, 100)
            }
        } catch (e: Exception) {
            println("LOAD READING PROGRESS FAILED: ${e.message}")
            e.printStackTrace()
        }
    }

    fun statusFor(bookId: Long): BookStatus {
        val progress = progressByBook[bookId] ?: 0
        return when {
            progress >= 100 -> BookStatus.READ
            progress > 0 -> BookStatus.READING
            else -> BookStatus.UNREAD
        }
    }

    val displayBooks = books.map { book ->
        book.copy(status = statusFor(book.bookId))
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val totalBooks = displayBooks.size
    val readCount = displayBooks.count { it.status == BookStatus.READ }
    val readingCount = displayBooks.count { it.status == BookStatus.READING }
    val unreadCount = displayBooks.count { it.status == BookStatus.UNREAD }

    // Tổng tiến độ = trung bình % của tất cả sách.
    val readingProgress = if (totalBooks == 0) {
        0f
    } else {
        displayBooks.sumOf { progressByBook[it.bookId] ?: 0 }.toFloat() /
                (totalBooks * 100f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = readingProgress.coerceIn(0f, 1f),
        animationSpec = tween(700),
        label = "readingProgress"
    )
    val progressPercent = (animatedProgress * 100).toInt()
    val selectedBook = displayBooks.firstOrNull { it.bookId == selectedBookId }

    val filteredBooks = displayBooks
        .filter { book ->
            when (selectedFilter) {
                BookFilter.ALL -> true
                BookFilter.READING -> book.status == BookStatus.READING
                BookFilter.READ -> book.status == BookStatus.READ
                BookFilter.UNREAD -> book.status == BookStatus.UNREAD
            }
        }
        .filter { book ->
            if (searchQuery.isBlank()) {
                true
            } else {
                val titleMatch = book.title.contains(searchQuery, ignoreCase = true)
                val authorMatch = book.author?.authorName?.contains(
                    searchQuery,
                    ignoreCase = true
                ) == true
                titleMatch || authorMatch
            }
        }

    val topBooks = displayBooks
        .sortedByDescending { book -> viewCounts[book.bookId] ?: 0 }
        .take(5)

    fun saveProgress(bookId: Long, value: Int, closeDialog: Boolean = false) {
        scope.launch {
            try {
                val saved = readingProgressRepository.updateProgress(
                    memberId = memberId,
                    bookId = bookId,
                    progress = value.coerceIn(0, 100)
                )
                progressByBook[bookId] = saved.progress.coerceIn(0, 100)
                if (closeDialog) selectedBookId = null
            } catch (e: Exception) {
                println("UPDATE READING PROGRESS FAILED: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun markBookAsRead(bookId: Long, closeDialog: Boolean = false) {
        scope.launch {
            try {
                val saved = readingProgressRepository.markAsRead(
                    memberId = memberId,
                    bookId = bookId
                )
                progressByBook[bookId] = saved.progress.coerceIn(0, 100)
                if (closeDialog) selectedBookId = null
            } catch (e: Exception) {
                println("MARK AS READ FAILED: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            PremiumDrawer(
                currentPage = currentPage,
                onPageSelected = { page ->
                    currentPage = page
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            containerColor = AppBackground,
            topBar = {
                PremiumTopBar(
                    currentPage = currentPage,
                    totalBooks = totalBooks,
                    searchMode = searchMode,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onSearchClick = {
                        if (searchMode) searchQuery = ""
                        searchMode = !searchMode
                    }
                )
            },
            floatingActionButton = {
                if (currentPage == DashboardPage.HOME) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FloatingActionButton(
                            onClick = onNavigateToAiSearch,
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(elevation = 10.dp, shape = CircleShape),
                            containerColor = DeepPurple,
                            contentColor = Color.White,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "AI",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            )
                        }

                        FloatingActionButton(
                            onClick = onNavigateToAddBook,
                            modifier = Modifier
                                .size(64.dp)
                                .shadow(elevation = 12.dp, shape = CircleShape),
                            containerColor = PrimaryPurple,
                            contentColor = Color.White,
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Thêm sách",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        ) { padding ->
            when (currentPage) {
                DashboardPage.HOME -> {
                    PremiumHomePage(
                        modifier = Modifier.padding(padding),
                        books = filteredBooks,
                        topBooks = topBooks,
                        viewCounts = viewCounts,
                        progressByBook = progressByBook,
                        totalBooks = totalBooks,
                        readCount = readCount,
                        readingCount = readingCount,
                        unreadCount = unreadCount,
                        animatedProgress = animatedProgress,
                        progressPercent = progressPercent,
                        selectedFilter = selectedFilter,
                        onFilterChange = { selectedFilter = it },
                        onBookClick = { book ->
                            selectedBookId = book.bookId
                            viewCounts[book.bookId] = (viewCounts[book.bookId] ?: 0) + 1
                        },
                        onMarkAsRead = { id -> markBookAsRead(id.toLong()) }
                    )
                }

                DashboardPage.ACCOUNT -> {
                    PremiumAccountPage(
                        modifier = Modifier.padding(padding),
                        memberId = memberId,
                        totalBooks = totalBooks,
                        readCount = readCount,
                        readingCount = readingCount
                    )
                }

                DashboardPage.SETTINGS -> {
                    PremiumSettingsPage(modifier = Modifier.padding(padding))
                }
            }
        }

        selectedBook?.let { book ->
            val currentProgress = progressByBook[book.bookId] ?: 0
            PremiumBookDialog(
                book = book,
                views = viewCounts[book.bookId] ?: 0,
                progress = currentProgress,
                onDismiss = { selectedBookId = null },
                onProgressChange = { value ->
                    saveProgress(book.bookId, value)
                },
                onMarkAsRead = {
                    markBookAsRead(book.bookId, closeDialog = true)
                }
            )
        }
    }
}


// ======================================================
// DRAWER
// ======================================================

@Composable
private fun PremiumDrawer(
    currentPage: DashboardPage,
    onPageSelected: (DashboardPage) -> Unit
) {

    ModalDrawerSheet(
        drawerContainerColor = SurfaceWhite
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp)
        ) {

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Row(
                modifier =
                    Modifier.padding(horizontal = 10.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier =
                        Modifier
                            .size(56.dp)
                            .background(
                                brush =
                                    Brush.linearGradient(
                                        listOf(
                                            PrimaryPurple,
                                            DeepPurple
                                        )
                                    ),

                                shape =
                                    RoundedCornerShape(18.dp)
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.MenuBook,

                        contentDescription =
                            null,

                        tint =
                            Color.White,

                        modifier =
                            Modifier.size(31.dp)
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(14.dp)
                )

                Column {

                    Text(
                        text = "ALFM",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Library Manager",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(28.dp)
            )

            HorizontalDivider(
                color =
                    Color(0xFFEDEDF2)
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            PremiumDrawerItem(
                title = "Trang chủ",
                subtitle = "Tổng quan thư viện",
                icon = Icons.Default.Home,
                selected =
                    currentPage == DashboardPage.HOME,
                onClick = {
                    onPageSelected(
                        DashboardPage.HOME
                    )
                }
            )

            PremiumDrawerItem(
                title = "Tài khoản",
                subtitle = "Thông tin cá nhân",
                icon = Icons.Default.Person,
                selected =
                    currentPage == DashboardPage.ACCOUNT,
                onClick = {
                    onPageSelected(
                        DashboardPage.ACCOUNT
                    )
                }
            )

            PremiumDrawerItem(
                title = "Cài đặt",
                subtitle = "Tuỳ chỉnh ứng dụng",
                icon = Icons.Default.Settings,
                selected =
                    currentPage == DashboardPage.SETTINGS,
                onClick = {
                    onPageSelected(
                        DashboardPage.SETTINGS
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.weight(1f)
            )

            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            VerySoftPurple
                    )
            ) {

                Row(
                    modifier =
                        Modifier.padding(15.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Info,

                        contentDescription =
                            null,

                        tint =
                            PrimaryPurple
                    )

                    Spacer(
                        modifier =
                            Modifier.width(10.dp)
                    )

                    Column {

                        Text(
                            text =
                                "ALFM Library",

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "Version 1.0",

                            fontSize =
                                11.sp,

                            color =
                                TextSecondary
                        )
                    }
                }
            }
        }
    }
}


// ======================================================
// DRAWER ITEM
// ======================================================

@Composable
private fun PremiumDrawerItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {

    NavigationDrawerItem(

        selected = selected,

        onClick = onClick,

        icon = {

            Box(
                modifier =
                    Modifier
                        .size(42.dp)
                        .background(
                            color =
                                if (selected)
                                    PrimaryPurple
                                else
                                    VerySoftPurple,

                            shape =
                                RoundedCornerShape(13.dp)
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector = icon,

                    contentDescription =
                        null,

                    tint =
                        if (selected)
                            Color.White
                        else
                            PrimaryPurple
                )
            }
        },

        label = {

            Column {

                Text(
                    text = title,

                    fontWeight =
                        if (selected)
                            FontWeight.Bold
                        else
                            FontWeight.Medium
                )

                Text(
                    text = subtitle,

                    fontSize = 11.sp,

                    color = TextSecondary
                )
            }
        },

        colors =
            androidx.compose.material3
                .NavigationDrawerItemDefaults
                .colors(
                    selectedContainerColor =
                        SoftPurple,

                    unselectedContainerColor =
                        Color.Transparent
                ),

        shape =
            RoundedCornerShape(18.dp),

        modifier =
            Modifier.padding(vertical = 3.dp)
    )
}


// ======================================================
// TOP BAR
// ======================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PremiumTopBar(
    currentPage: DashboardPage,
    totalBooks: Int,
    searchMode: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit
) {

    TopAppBar(

        navigationIcon = {

            IconButton(
                onClick = onMenuClick
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Menu,

                    contentDescription =
                        "Menu",

                    tint =
                        Color.White
                )
            }
        },

        title = {

            if (
                currentPage == DashboardPage.HOME &&
                searchMode
            ) {

                TextField(

                    value =
                        searchQuery,

                    onValueChange =
                        onSearchQueryChange,

                    placeholder = {

                        Text(
                            text =
                                "Tìm sách, tác giả...",

                            color =
                                Color.White.copy(
                                    alpha = 0.7f
                                )
                        )
                    },

                    singleLine = true,

                    colors =
                        TextFieldDefaults.colors(

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
                        text =
                            when (currentPage) {

                                DashboardPage.HOME ->
                                    "Thư viện của tôi"

                                DashboardPage.ACCOUNT ->
                                    "Tài khoản"

                                DashboardPage.SETTINGS ->
                                    "Cài đặt"
                            },

                        fontWeight =
                            FontWeight.Bold,

                        fontSize =
                            19.sp
                    )

                    if (
                        currentPage ==
                        DashboardPage.HOME
                    ) {

                        Text(
                            text =
                                "$totalBooks cuốn sách",

                            fontSize =
                                11.sp,

                            color =
                                Color.White.copy(
                                    alpha = 0.78f
                                )
                        )
                    }
                }
            }
        },

        actions = {

            if (
                currentPage ==
                DashboardPage.HOME
            ) {

                IconButton(
                    onClick =
                        onSearchClick
                ) {

                    Icon(
                        imageVector =
                            if (searchMode)
                                Icons.Default.Close
                            else
                                Icons.Default.Search,

                        contentDescription =
                            "Search",

                        tint =
                            Color.White
                    )
                }
            }
        },

        colors =
            TopAppBarDefaults
                .topAppBarColors(
                    containerColor =
                        PrimaryPurple,

                    titleContentColor =
                        Color.White
                )
    )
}


// ======================================================
// HOME PAGE
// ======================================================

@Composable
private fun PremiumHomePage(
    modifier: Modifier,
    books: List<Book>,
    topBooks: List<Book>,
    viewCounts: Map<Long, Int>,
    progressByBook: Map<Long, Int>,
    totalBooks: Int,
    readCount: Int,
    readingCount: Int,
    unreadCount: Int,
    animatedProgress: Float,
    progressPercent: Int,
    selectedFilter: BookFilter,
    onFilterChange: (BookFilter) -> Unit,
    onBookClick: (Book) -> Unit,
    onMarkAsRead: (Int) -> Unit
) {

    LazyColumn(

        modifier =
            modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 18.dp,
                bottom = 110.dp
            ),

        verticalArrangement =
            Arrangement.spacedBy(18.dp)
    ) {

        item {

            HeroWelcomeCard(
                readCount =
                    readCount,

                totalBooks =
                    totalBooks
            )
        }

        item {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(11.dp)
            ) {

                PremiumStatCard(
                    modifier =
                        Modifier.weight(1f),

                    title =
                        "Tổng sách",

                    value =
                        totalBooks.toString(),

                    icon =
                        Icons.Default.MenuBook,

                    background =
                        SoftPurple,

                    iconColor =
                        PrimaryPurple
                )

                PremiumStatCard(
                    modifier =
                        Modifier.weight(1f),

                    title =
                        "Đã đọc",

                    value =
                        readCount.toString(),

                    icon =
                        Icons.Default.CheckCircle,

                    background =
                        SoftGreen,

                    iconColor =
                        SuccessGreen
                )
            }

            Spacer(
                modifier =
                    Modifier.height(11.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(11.dp)
            ) {

                PremiumStatCard(
                    modifier =
                        Modifier.weight(1f),

                    title =
                        "Đang đọc",

                    value =
                        readingCount.toString(),

                    icon =
                        Icons.Default.AutoStories,

                    background =
                        VerySoftPurple,

                    iconColor =
                        DeepPurple
                )

                PremiumStatCard(
                    modifier =
                        Modifier.weight(1f),

                    title =
                        "Chưa đọc",

                    value =
                        unreadCount.toString(),

                    icon =
                        Icons.Default.BookmarkBorder,

                    background =
                        SoftOrange,

                    iconColor =
                        WarningOrange
                )
            }
        }

        item {

            PremiumProgressCard(
                progress =
                    animatedProgress,

                percent =
                    progressPercent,

                readCount =
                    readCount,

                totalBooks =
                    totalBooks
            )
        }

        val continueBook =
            books.firstOrNull {
                it.status ==
                        BookStatus.READING
            }

        if (continueBook != null) {

            item {

                SectionHeader(
                    title =
                        "Tiếp tục đọc",

                    subtitle =
                        "Đừng để cuốn sách đang dang dở"
                )
            }

            item {

                ContinueReadingCard(
                    book = continueBook,
                    progress = progressByBook[continueBook.bookId] ?: 0,
                    onClick = {
                        onBookClick(continueBook)
                    }
                )
            }
        }

        item {

            SectionHeader(
                title =
                    "🔥 Top sách nổi bật",

                subtitle =
                    "Được xem nhiều nhất"
            )
        }

        if (topBooks.isNotEmpty()) {

            item {

                LazyRow(
                    horizontalArrangement =
                        Arrangement.spacedBy(13.dp),

                    contentPadding =
                        PaddingValues(end = 10.dp)
                ) {

                    items(
                        items = topBooks,

                        key = { book ->
                            book.bookId
                                ?: book.title
                                    .hashCode()
                                    .toLong()
                        }
                    ) { book ->

                        val rank =
                            topBooks.indexOf(book) + 1

                        PremiumTopBookCard(

                            book =
                                book,

                            rank =
                                rank,

                            views =
                                viewCounts[
                                    book.bookId
                                ] ?: 0,

                            onClick = {
                                onBookClick(book)
                            }
                        )
                    }
                }
            }
        }

        item {

            SectionHeader(
                title =
                    "Sách của tôi",

                subtitle =
                    "${books.size} sách đang hiển thị"
            )
        }

        item {

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(
                            rememberScrollState()
                        ),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                PremiumFilterChip(
                    title =
                        "Tất cả",

                    selected =
                        selectedFilter ==
                                BookFilter.ALL,

                    onClick = {
                        onFilterChange(
                            BookFilter.ALL
                        )
                    }
                )

                PremiumFilterChip(
                    title =
                        "Đang đọc",

                    selected =
                        selectedFilter ==
                                BookFilter.READING,

                    onClick = {
                        onFilterChange(
                            BookFilter.READING
                        )
                    }
                )

                PremiumFilterChip(
                    title =
                        "Đã đọc",

                    selected =
                        selectedFilter ==
                                BookFilter.READ,

                    onClick = {
                        onFilterChange(
                            BookFilter.READ
                        )
                    }
                )

                PremiumFilterChip(
                    title =
                        "Chưa đọc",

                    selected =
                        selectedFilter ==
                                BookFilter.UNREAD,

                    onClick = {
                        onFilterChange(
                            BookFilter.UNREAD
                        )
                    }
                )
            }
        }

        if (books.isEmpty()) {

            item {
                PremiumEmptyLibrary()
            }

        } else {

            items(

                items = books,

                key = { book ->
                    book.bookId
                        ?: book.title
                            .hashCode()
                            .toLong()
                }

            ) { book ->

                PremiumBookListItem(

                    book =
                        book,

                    views =
                        viewCounts[
                            book.bookId
                        ] ?: 0,

                    onClick = {
                        onBookClick(book)
                    },

                    onMarkAsRead = {

                        book.bookId?.let { id ->
                            onMarkAsRead(
                                id.toInt()
                            )
                        }
                    }
                )
            }
        }
    }
}


// ======================================================
// HERO
// ======================================================

@Composable
private fun HeroWelcomeCard(
    readCount: Int,
    totalBooks: Int
) {

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape =
                        RoundedCornerShape(28.dp)
                ),

        shape =
            RoundedCornerShape(28.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.Transparent
            )
    ) {

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        brush =
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF7D5CE7),
                                    Color(0xFF5C45B9)
                                )
                            )
                    )
                    .padding(23.dp)
        ) {

            Column {

                Surface(
                    color =
                        Color.White.copy(
                            alpha = 0.16f
                        ),

                    shape =
                        RoundedCornerShape(50.dp)
                ) {

                    Text(
                        text =
                            "📚  ALFM Library",

                        color =
                            Color.White,

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.SemiBold,

                        modifier =
                            Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(17.dp)
                )

                Text(
                    text =
                        "Xin chào 👋",

                    color =
                        Color.White,

                    fontSize =
                        27.sp,

                    fontWeight =
                        FontWeight.ExtraBold
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(
                    text =
                        "Mỗi trang sách là một hành trình mới.",

                    color =
                        Color.White.copy(
                            alpha = 0.84f
                        ),

                    fontSize =
                        14.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier =
                            Modifier
                                .size(43.dp)
                                .background(
                                    Color.White.copy(
                                        alpha = 0.16f
                                    ),
                                    CircleShape
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.AutoStories,

                            contentDescription =
                                null,

                            tint =
                                Color.White
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(11.dp)
                    )

                    Column {

                        Text(
                            text =
                                "$readCount / $totalBooks cuốn",

                            color =
                                Color.White,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "đã hoàn thành",

                            color =
                                Color.White.copy(
                                    alpha = 0.75f
                                ),

                            fontSize =
                                11.sp
                        )
                    }
                }
            }
        }
    }
}


// ======================================================
// STAT CARD
// ======================================================

@Composable
private fun PremiumStatCard(
    modifier: Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    background: Color,
    iconColor: Color
) {

    Card(
        modifier =
            modifier
                .height(115.dp)
                .shadow(
                    3.dp,
                    RoundedCornerShape(22.dp)
                ),

        shape =
            RoundedCornerShape(22.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    SurfaceWhite
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(15.dp),

            verticalArrangement =
                Arrangement.SpaceBetween
        ) {

            Box(
                modifier =
                    Modifier
                        .size(39.dp)
                        .background(
                            background,
                            RoundedCornerShape(13.dp)
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        icon,

                    contentDescription =
                        null,

                    tint =
                        iconColor,

                    modifier =
                        Modifier.size(21.dp)
                )
            }

            Row(
                verticalAlignment =
                    Alignment.Bottom
            ) {

                Text(
                    text =
                        value,

                    fontSize =
                        25.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        TextPrimary
                )

                Spacer(
                    modifier =
                        Modifier.width(7.dp)
                )

                Text(
                    text =
                        title,

                    fontSize =
                        11.sp,

                    color =
                        TextSecondary,

                    modifier =
                        Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}


// ======================================================
// PROGRESS
// ======================================================

@Composable
private fun PremiumProgressCard(
    progress: Float,
    percent: Int,
    readCount: Int,
    totalBooks: Int
) {

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .shadow(
                    3.dp,
                    RoundedCornerShape(24.dp)
                ),

        shape =
            RoundedCornerShape(24.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    SurfaceWhite
            )
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier =
                            Modifier
                                .size(46.dp)
                                .background(
                                    SoftPurple,
                                    RoundedCornerShape(15.dp)
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.AutoStories,

                            contentDescription =
                                null,

                            tint =
                                PrimaryPurple
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(12.dp)
                    )

                    Column {

                        Text(
                            text =
                                "Tiến độ đọc",

                            fontWeight =
                                FontWeight.Bold,

                            fontSize =
                                16.sp
                        )

                        Text(
                            text =
                                "$readCount/$totalBooks cuốn sách",

                            color =
                                TextSecondary,

                            fontSize =
                                12.sp
                        )
                    }
                }

                Surface(
                    color =
                        SoftPurple,

                    shape =
                        CircleShape
                ) {

                    Text(
                        text =
                            "$percent%",

                        color =
                            PrimaryPurple,

                        fontWeight =
                            FontWeight.ExtraBold,

                        modifier =
                            Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            LinearProgressIndicator(

                progress = {
                    progress
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(
                            RoundedCornerShape(20.dp)
                        ),

                color =
                    PrimaryPurple,

                trackColor =
                    SoftPurple
            )
        }
    }
}


// ======================================================
// SECTION HEADER
// ======================================================

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween,

        verticalAlignment =
            Alignment.Bottom
    ) {

        Column {

            Text(
                text =
                    title,

                fontSize =
                    20.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    TextPrimary
            )

            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )

            Text(
                text =
                    subtitle,

                fontSize =
                    11.sp,

                color =
                    TextSecondary
            )
        }
    }
}


// ======================================================
// CONTINUE READING
// ======================================================

@Composable
private fun ContinueReadingCard(
    book: Book,
    progress: Int,
    onClick: () -> Unit
) {

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
                .shadow(
                    4.dp,
                    RoundedCornerShape(24.dp)
                ),

        shape =
            RoundedCornerShape(24.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    SurfaceWhite
            )
    ) {

        Row(
            modifier =
                Modifier.padding(15.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            PremiumBookCover(
                book = book,
                width = 76,
                height = 104
            )

            Spacer(
                modifier =
                    Modifier.width(15.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Surface(
                    color =
                        SoftPurple,

                    shape =
                        RoundedCornerShape(50.dp)
                ) {

                    Text(
                        text =
                            "ĐANG ĐỌC",

                        color =
                            PrimaryPurple,

                        fontSize =
                            9.sp,

                        fontWeight =
                            FontWeight.Bold,

                        modifier =
                            Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 4.dp
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        book.title,

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        17.sp,

                    maxLines =
                        2,

                    overflow =
                        TextOverflow.Ellipsis
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text =
                        book.author
                            ?.authorName
                            ?: "Chưa rõ tác giả",

                    color =
                        TextSecondary,

                    fontSize =
                        12.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                LinearProgressIndicator(

                    progress = {
                        progress.coerceIn(0, 100) / 100f
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),

                    color =
                        PrimaryPurple,

                    trackColor =
                        SoftPurple
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(
                    text =
                        "Đã đọc ${progress.coerceIn(0, 100)}%  •  Tiếp tục →",

                    fontSize =
                        11.sp,

                    color =
                        PrimaryPurple,

                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }
    }
}


// ======================================================
// TOP BOOK
// ======================================================

@Composable
private fun PremiumTopBookCard(
    book: Book,
    rank: Int,
    views: Int,
    onClick: () -> Unit
) {

    Card(
        modifier =
            Modifier
                .width(162.dp)
                .clickable {
                    onClick()
                }
                .shadow(
                    4.dp,
                    RoundedCornerShape(22.dp)
                ),

        shape =
            RoundedCornerShape(22.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    SurfaceWhite
            )
    ) {

        Column {

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(185.dp)
            ) {

                PremiumBookCover(
                    book = book,
                    modifier =
                        Modifier.fillMaxSize()
                )

                Surface(
                    modifier =
                        Modifier
                            .align(
                                Alignment.TopStart
                            )
                            .padding(9.dp),

                    color =
                        if (rank == 1)
                            Color(0xFFFFC94A)
                        else
                            PrimaryPurple,

                    shape =
                        RoundedCornerShape(50.dp)
                ) {

                    Text(
                        text =
                            "#$rank",

                        color =
                            Color.White,

                        fontWeight =
                            FontWeight.ExtraBold,

                        fontSize =
                            12.sp,

                        modifier =
                            Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 5.dp
                            )
                    )
                }

                Surface(
                    modifier =
                        Modifier
                            .align(
                                Alignment.TopEnd
                            )
                            .padding(9.dp),

                    color =
                        Color.Black.copy(
                            alpha = 0.58f
                        ),

                    shape =
                        RoundedCornerShape(50.dp)
                ) {

                    Row(
                        modifier =
                            Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 5.dp
                            ),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Visibility,

                            contentDescription =
                                null,

                            tint =
                                Color.White,

                            modifier =
                                Modifier.size(13.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(4.dp)
                        )

                        Text(
                            text =
                                "$views",

                            color =
                                Color.White,

                            fontSize =
                                10.sp
                        )
                    }
                }
            }

            Column(
                modifier =
                    Modifier.padding(12.dp)
            ) {

                Text(
                    text =
                        book.title,

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        14.sp,

                    maxLines =
                        1,

                    overflow =
                        TextOverflow.Ellipsis
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        book.author
                            ?.authorName
                            ?: "Chưa rõ tác giả",

                    color =
                        TextSecondary,

                    fontSize =
                        11.sp,

                    maxLines =
                        1,

                    overflow =
                        TextOverflow.Ellipsis
                )
            }
        }
    }
}


// ======================================================
// FILTER
// ======================================================

@Composable
private fun PremiumFilterChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    FilterChip(

        selected =
            selected,

        onClick =
            onClick,

        label = {

            Text(
                text =
                    title,

                fontWeight =
                    if (selected)
                        FontWeight.Bold
                    else
                        FontWeight.Normal
            )
        },

        shape =
            RoundedCornerShape(50.dp),

        colors =
            FilterChipDefaults
                .filterChipColors(

                    selectedContainerColor =
                        PrimaryPurple,

                    selectedLabelColor =
                        Color.White,

                    containerColor =
                        SurfaceWhite,

                    labelColor =
                        TextSecondary
                )
    )
}


// ======================================================
// BOOK LIST
// ======================================================

@Composable
private fun PremiumBookListItem(
    book: Book,
    views: Int,
    onClick: () -> Unit,
    onMarkAsRead: () -> Unit
) {

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
                .shadow(
                    3.dp,
                    RoundedCornerShape(22.dp)
                )
                .animateContentSize(),

        shape =
            RoundedCornerShape(22.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    SurfaceWhite
            )
    ) {

        Row(
            modifier =
                Modifier.padding(14.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            PremiumBookCover(
                book = book,
                width = 66,
                height = 88
            )

            Spacer(
                modifier =
                    Modifier.width(14.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        book.title,

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        15.sp,

                    maxLines =
                        1,

                    overflow =
                        TextOverflow.Ellipsis
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text =
                        book.author
                            ?.authorName
                            ?: "Chưa rõ tác giả",

                    color =
                        TextSecondary,

                    fontSize =
                        12.sp,

                    maxLines =
                        1,

                    overflow =
                        TextOverflow.Ellipsis
                )

                Spacer(
                    modifier =
                        Modifier.height(11.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    BookStatusBadge(
                        status =
                            book.status
                    )

                    Spacer(
                        modifier =
                            Modifier.width(10.dp)
                    )

                    Icon(
                        imageVector =
                            Icons.Default.Visibility,

                        contentDescription =
                            null,

                        tint =
                            TextSecondary,

                        modifier =
                            Modifier.size(14.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(4.dp)
                    )

                    Text(
                        text =
                            "$views",

                        color =
                            TextSecondary,

                        fontSize =
                            11.sp
                    )
                }
            }

            if (
                book.status !=
                BookStatus.READ
            ) {

                IconButton(
                    onClick =
                        onMarkAsRead
                ) {

                    Box(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .background(
                                    SoftPurple,
                                    CircleShape
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Check,

                            contentDescription =
                                "Đánh dấu đã đọc",

                            tint =
                                PrimaryPurple
                        )
                    }
                }

            } else {

                Box(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .background(
                                SoftGreen,
                                CircleShape
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.CheckCircle,

                        contentDescription =
                            null,

                        tint =
                            SuccessGreen
                    )
                }
            }
        }
    }
}


// ======================================================
// BOOK COVER
// ======================================================

@Composable
private fun PremiumBookCover(
    book: Book,
    modifier: Modifier = Modifier,
    width: Int = 70,
    height: Int = 95
) {

    val finalModifier =
        if (modifier == Modifier) {

            Modifier
                .width(width.dp)
                .height(height.dp)
                .clip(
                    RoundedCornerShape(15.dp)
                )

        } else {

            modifier.clip(
                RoundedCornerShape(15.dp)
            )
        }

    if (!book.imageUrl.isNullOrBlank()) {

        AsyncImage(
            model =
                book.imageUrl,

            contentDescription =
                book.title,

            modifier =
                finalModifier,

            contentScale =
                ContentScale.Crop
        )

    } else {

        Box(
            modifier =
                finalModifier
                    .background(
                        brush =
                            Brush.linearGradient(
                                listOf(
                                    SoftPurple,
                                    Color(0xFFDAD0FF)
                                )
                            )
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector =
                    Icons.Default.AutoStories,

                contentDescription =
                    null,

                tint =
                    PrimaryPurple,

                modifier =
                    Modifier.size(31.dp)
            )
        }
    }
}


// ======================================================
// STATUS BADGE
// ======================================================

@Composable
private fun BookStatusBadge(
    status: BookStatus
) {

    val title: String
    val background: Color
    val textColor: Color

    when (status) {

        BookStatus.READ -> {

            title =
                "Đã đọc"

            background =
                SoftGreen

            textColor =
                SuccessGreen
        }

        BookStatus.READING -> {

            title =
                "Đang đọc"

            background =
                SoftPurple

            textColor =
                PrimaryPurple
        }

        BookStatus.UNREAD -> {

            title =
                "Chưa đọc"

            background =
                SoftOrange

            textColor =
                WarningOrange
        }
    }

    Surface(
        color =
            background,

        shape =
            RoundedCornerShape(50.dp)
    ) {

        Text(
            text =
                title,

            color =
                textColor,

            fontWeight =
                FontWeight.SemiBold,

            fontSize =
                10.sp,

            modifier =
                Modifier.padding(
                    horizontal = 9.dp,
                    vertical = 5.dp
                )
        )
    }
}


// ======================================================
// EMPTY
// ======================================================

@Composable
private fun PremiumEmptyLibrary() {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(25.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    SurfaceWhite
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 45.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier =
                    Modifier
                        .size(76.dp)
                        .background(
                            SoftPurple,
                            CircleShape
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.MenuBook,

                    contentDescription =
                        null,

                    tint =
                        PrimaryPurple,

                    modifier =
                        Modifier.size(38.dp)
                )
            }

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            Text(
                text =
                    "Thư viện đang trống",

                fontWeight =
                    FontWeight.Bold,

                fontSize =
                    17.sp
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "Nhấn nút + để thêm cuốn sách đầu tiên",

                color =
                    TextSecondary,

                fontSize =
                    12.sp
            )
        }
    }
}


// ======================================================
// BOOK DIALOG
// ======================================================

@Composable
private fun PremiumBookDialog(
    book: Book,
    views: Int,
    progress: Int,
    onDismiss: () -> Unit,
    onProgressChange: (Int) -> Unit,
    onMarkAsRead: () -> Unit
) {
    val safeProgress = progress.coerceIn(0, 100)

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        title = {
            Column {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = book.author?.authorName ?: "Chưa rõ tác giả",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BookStatusBadge(status = book.status)
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "$views lượt xem",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tiến độ đọc",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$safeProgress%",
                        color = PrimaryPurple,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { safeProgress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp)
                        .clip(CircleShape),
                    color = PrimaryPurple,
                    trackColor = SoftPurple
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        enabled = safeProgress > 0,
                        onClick = {
                            onProgressChange((safeProgress - 10).coerceAtLeast(0))
                        }
                    ) {
                        Text("-10%")
                    }

                    Text(
                        text = when {
                            safeProgress >= 100 -> "Đã đọc xong"
                            safeProgress > 0 -> "Đang đọc"
                            else -> "Chưa đọc"
                        },
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    TextButton(
                        enabled = safeProgress < 100,
                        onClick = {
                            onProgressChange((safeProgress + 10).coerceAtMost(100))
                        }
                    ) {
                        Text("+10%")
                    }
                }

                if (!book.category?.categoryName.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Thể loại",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = book.category?.categoryName ?: "",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        confirmButton = {
            if (book.status != BookStatus.READ) {
                Button(
                    onClick = onMarkAsRead,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Đánh dấu đã đọc")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Đóng")
            }
        }
    )
}


// ======================================================
// ACCOUNT PAGE
// ======================================================

@Composable
private fun PremiumAccountPage(
    modifier: Modifier,
    memberId: Long,
    totalBooks: Int,
    readCount: Int,
    readingCount: Int
) {

    val memberRepository =
        remember {
            MemberRepository()
        }

    val scope =
        rememberCoroutineScope()

    var editing by remember {
        mutableStateOf(false)
    }

    var fullName by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    // Những field này hiện chỉ hiển thị trên Android.
    // Chưa lưu Oracle vì LIB_MEMBERS chưa có column tương ứng.
    var birthday by remember {
        mutableStateOf("")
    }

    var address by remember {
        mutableStateOf("")
    }

    var favoriteGenre by remember {
        mutableStateOf("Tiểu thuyết")
    }

    var yearlyGoal by remember {
        mutableStateOf("20")
    }

    var bio by remember {
        mutableStateOf(
            "Yêu thích đọc sách và khám phá những điều mới."
        )
    }

    var message by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var isSaving by remember {
        mutableStateOf(false)
    }


    // ==================================================
    // LOAD MEMBER FROM ORACLE
    // ==================================================

    LaunchedEffect(memberId) {

        isLoading = true
        message = ""

        try {

            val member =
                memberRepository.getMember(
                    memberId
                )

            fullName =
                member.fullName

            email =
                member.email ?: ""

            phone =
                member.phone ?: ""

            isLoading = false

        } catch (e: Exception) {

            isLoading = false

            message =
                "Không thể tải thông tin tài khoản"
        }
    }


    LazyColumn(

        modifier =
            modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 18.dp,
                bottom = 35.dp
            ),

        verticalArrangement =
            Arrangement.spacedBy(17.dp)
    ) {

        // ==================================================
        // PROFILE HEADER
        // ==================================================

        item {

            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .shadow(
                            8.dp,
                            RoundedCornerShape(30.dp)
                        ),

                shape =
                    RoundedCornerShape(30.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.Transparent
                    )
            ) {

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        PrimaryPurple,
                                        DeepPurple
                                    )
                                )
                            )
                            .padding(24.dp)
                ) {

                    Column(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier =
                                Modifier
                                    .size(94.dp)
                                    .background(
                                        Color.White.copy(
                                            alpha = 0.17f
                                        ),
                                        CircleShape
                                    ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Box(
                                modifier =
                                    Modifier
                                        .size(78.dp)
                                        .background(
                                            Color.White,
                                            CircleShape
                                        ),

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.Person,

                                    contentDescription =
                                        null,

                                    tint =
                                        PrimaryPurple,

                                    modifier =
                                        Modifier.size(46.dp)
                                )
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier.height(13.dp)
                        )

                        if (isLoading) {

                            CircularProgressIndicator(
                                color =
                                    Color.White,

                                modifier =
                                    Modifier.size(28.dp)
                            )

                        } else {

                            Text(
                                text =
                                    if (fullName.isBlank())
                                        "Người dùng"
                                    else
                                        fullName,

                                color =
                                    Color.White,

                                fontSize =
                                    23.sp,

                                fontWeight =
                                    FontWeight.ExtraBold
                            )

                            Text(
                                text =
                                    email,

                                color =
                                    Color.White.copy(
                                        alpha = 0.78f
                                    ),

                                fontSize =
                                    12.sp
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(22.dp)
                        )

                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.SpaceEvenly
                        ) {

                            ProfileStat(
                                value =
                                    totalBooks.toString(),

                                title =
                                    "Tổng sách"
                            )

                            ProfileDivider()

                            ProfileStat(
                                value =
                                    readCount.toString(),

                                title =
                                    "Đã đọc"
                            )

                            ProfileDivider()

                            ProfileStat(
                                value =
                                    readingCount.toString(),

                                title =
                                    "Đang đọc"
                            )
                        }
                    }
                }
            }
        }


        // ==================================================
        // PERSONAL INFORMATION HEADER
        // ==================================================

        item {

            ProfileSectionHeader(

                title =
                    "Thông tin cá nhân",

                icon =
                    Icons.Default.Person,

                actionText =
                    if (editing)
                        null
                    else
                        "Chỉnh sửa",

                onAction = {

                    editing = true
                    message = ""
                }
            )
        }


        // ==================================================
        // PERSONAL INFORMATION
        // ==================================================

        item {

            ProfileSectionCard {

                ProfileInput(

                    title =
                        "Họ và tên",

                    value =
                        fullName,

                    enabled =
                        editing,

                    icon =
                        Icons.Default.Person,

                    onValueChange = {
                        fullName = it
                    }
                )


                ProfileInput(

                    title =
                        "Email",

                    value =
                        email,

                    enabled =
                        editing,

                    icon =
                        Icons.Default.Email,

                    keyboardType =
                        KeyboardType.Email,

                    onValueChange = {
                        email = it
                    }
                )


                ProfileInput(

                    title =
                        "Số điện thoại",

                    value =
                        phone,

                    enabled =
                        editing,

                    icon =
                        Icons.Default.Phone,

                    keyboardType =
                        KeyboardType.Phone,

                    onValueChange = {

                        phone =
                            it.filter { char ->
                                char.isDigit()
                            }
                    }
                )


                ProfileInput(

                    title =
                        "Ngày sinh",

                    value =
                        birthday,

                    enabled =
                        editing,

                    icon =
                        Icons.Default.Info,

                    placeholder =
                        "01/01/2005",

                    onValueChange = {
                        birthday = it
                    }
                )


                ProfileInput(

                    title =
                        "Địa chỉ",

                    value =
                        address,

                    enabled =
                        editing,

                    icon =
                        Icons.Default.LocationOn,

                    placeholder =
                        "Nhập địa chỉ",

                    onValueChange = {
                        address = it
                    }
                )
            }
        }


        // ==================================================
        // READING PREFERENCES
        // ==================================================

        item {

            ProfileSectionHeader(

                title =
                    "Sở thích đọc sách",

                icon =
                    Icons.Default.Favorite
            )
        }


        item {

            ProfileSectionCard {

                ProfileInput(

                    title =
                        "Thể loại yêu thích",

                    value =
                        favoriteGenre,

                    enabled =
                        editing,

                    icon =
                        Icons.Default.Favorite,

                    onValueChange = {
                        favoriteGenre = it
                    }
                )


                ProfileInput(

                    title =
                        "Mục tiêu sách / năm",

                    value =
                        yearlyGoal,

                    enabled =
                        editing,

                    icon =
                        Icons.Default.MenuBook,

                    keyboardType =
                        KeyboardType.Number,

                    onValueChange = {

                        yearlyGoal =
                            it.filter { char ->
                                char.isDigit()
                            }
                    }
                )


                OutlinedTextField(

                    value =
                        bio,

                    onValueChange = {
                        bio = it
                    },

                    enabled =
                        editing,

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text(
                            "Giới thiệu bản thân"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.Info,

                            contentDescription =
                                null,

                            tint =
                                PrimaryPurple
                        )
                    },

                    minLines =
                        3,

                    shape =
                        RoundedCornerShape(17.dp)
                )
            }
        }


        // ==================================================
        // SAVE BUTTON
        // ==================================================

        if (editing) {

            item {

                Button(

                    enabled =
                        !isSaving,

                    onClick = {

                        when {

                            fullName.isBlank() -> {

                                message =
                                    "Vui lòng nhập họ và tên"
                            }

                            email.isBlank() -> {

                                message =
                                    "Vui lòng nhập email"
                            }

                            else -> {

                                scope.launch {

                                    isSaving = true
                                    message = ""

                                    val result =
                                        memberRepository.updateMember(

                                            memberId =
                                                memberId,

                                            fullName =
                                                fullName,

                                            email =
                                                email,

                                            phone =
                                                phone
                                        )

                                    isSaving = false

                                    result.onSuccess { updatedMember ->

                                        fullName =
                                            updatedMember.fullName

                                        email =
                                            updatedMember.email
                                                ?: ""

                                        phone =
                                            updatedMember.phone
                                                ?: ""

                                        editing = false

                                        message =
                                            "Đã lưu thông tin tài khoản"

                                    }.onFailure { error ->

                                        message =
                                            error.message
                                                ?: "Không thể cập nhật thông tin"
                                    }
                                }
                            }
                        }
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp),

                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    PrimaryPurple
                            ),

                    shape =
                        RoundedCornerShape(17.dp)
                ) {

                    if (isSaving) {

                        CircularProgressIndicator(
                            color =
                                Color.White,

                            modifier =
                                Modifier.size(21.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Text(
                            text =
                                "Đang lưu..."
                        )

                    } else {

                        Icon(
                            imageVector =
                                Icons.Default.Save,

                            contentDescription =
                                null
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Text(
                            text =
                                "Lưu thay đổi",

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }


        // ==================================================
        // MESSAGE
        // ==================================================

        if (message.isNotBlank()) {

            item {

                Surface(

                    modifier =
                        Modifier.fillMaxWidth(),

                    color =
                        if (
                            message.startsWith("Đã")
                        )
                            SoftGreen
                        else
                            SoftOrange,

                    shape =
                        RoundedCornerShape(15.dp)
                ) {

                    Text(
                        text =
                            message,

                        modifier =
                            Modifier.padding(13.dp),

                        color =
                            if (
                                message.startsWith("Đã")
                            )
                                SuccessGreen
                            else
                                WarningOrange,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


// ======================================================
// PROFILE STAT
// ======================================================

@Composable
private fun ProfileStat(
    value: String,
    title: String
) {

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text =
                value,

            color =
                Color.White,

            fontSize =
                22.sp,

            fontWeight =
                FontWeight.ExtraBold
        )

        Text(
            text =
                title,

            color =
                Color.White.copy(
                    alpha = 0.72f
                ),

            fontSize =
                10.sp
        )
    }
}


@Composable
private fun ProfileDivider() {

    Box(
        modifier =
            Modifier
                .width(1.dp)
                .height(35.dp)
                .background(
                    Color.White.copy(
                        alpha = 0.22f
                    )
                )
    )
}


// ======================================================
// PROFILE HEADER
// ======================================================

@Composable
private fun ProfileSectionHeader(
    title: String,
    icon: ImageVector,
    actionText: String? = null,
    onAction: () -> Unit = {}
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier =
                Modifier
                    .size(38.dp)
                    .background(
                        SoftPurple,
                        RoundedCornerShape(12.dp)
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector =
                    icon,

                contentDescription =
                    null,

                tint =
                    PrimaryPurple,

                modifier =
                    Modifier.size(20.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.width(10.dp)
        )

        Text(
            text =
                title,

            modifier =
                Modifier.weight(1f),

            fontSize =
                18.sp,

            fontWeight =
                FontWeight.ExtraBold
        )

        if (actionText != null) {

            TextButton(
                onClick =
                    onAction
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Edit,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(16.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(4.dp)
                )

                Text(
                    text =
                        actionText
                )
            }
        }
    }
}


// ======================================================
// PROFILE CARD
// ======================================================

@Composable
private fun ProfileSectionCard(
    content: @Composable () -> Unit
) {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .shadow(
                    3.dp,
                    RoundedCornerShape(24.dp)
                ),

        shape =
            RoundedCornerShape(24.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    SurfaceWhite
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(13.dp)
        ) {

            content()
        }
    }
}


// ======================================================
// PROFILE INPUT
// ======================================================

@Composable
private fun ProfileInput(
    title: String,
    value: String,
    enabled: Boolean,
    icon: ImageVector,
    placeholder: String = "",
    keyboardType: KeyboardType =
        KeyboardType.Text,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(

        value =
            value,

        onValueChange =
            onValueChange,

        enabled =
            enabled,

        modifier =
            Modifier.fillMaxWidth(),

        label = {
            Text(
                text =
                    title
            )
        },

        placeholder = {

            if (placeholder.isNotBlank()) {

                Text(
                    text =
                        placeholder
                )
            }
        },

        leadingIcon = {

            Icon(
                imageVector =
                    icon,

                contentDescription =
                    null,

                tint =
                    PrimaryPurple
            )
        },

        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    keyboardType
            ),

        singleLine =
            true,

        shape =
            RoundedCornerShape(17.dp)
    )
}


// ======================================================
// SETTINGS
// ======================================================

@Composable
private fun PremiumSettingsPage(
    modifier: Modifier
) {

    var notifications by remember {
        mutableStateOf(true)
    }

    var readingReminder by remember {
        mutableStateOf(true)
    }

    var autoSuggestions by remember {
        mutableStateOf(true)
    }

    LazyColumn(

        modifier =
            modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 18.dp,
                bottom = 35.dp
            ),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        item {

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(25.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            VerySoftPurple
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(21.dp)
                ) {

                    Text(
                        text =
                            "Tuỳ chỉnh trải nghiệm ✨",

                        fontSize =
                            21.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            TextPrimary
                    )

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            "Điều chỉnh ALFM theo thói quen đọc sách của bạn.",

                        color =
                            TextSecondary,

                        fontSize =
                            12.sp
                    )
                }
            }
        }

        item {

            SettingsTitle(
                title =
                    "Thông báo"
            )
        }

        item {

            PremiumSettingSwitch(

                title =
                    "Thông báo ứng dụng",

                subtitle =
                    "Nhận các thông báo quan trọng",

                icon =
                    Icons.Default.Notifications,

                checked =
                    notifications,

                onCheckedChange = {
                    notifications = it
                }
            )
        }

        item {

            PremiumSettingSwitch(

                title =
                    "Nhắc đọc sách",

                subtitle =
                    "Duy trì thói quen đọc mỗi ngày",

                icon =
                    Icons.Default.Alarm,

                checked =
                    readingReminder,

                onCheckedChange = {
                    readingReminder = it
                }
            )
        }

        item {

            SettingsTitle(
                title =
                    "Thư viện"
            )
        }

        item {

            PremiumSettingSwitch(

                title =
                    "Gợi ý sách",

                subtitle =
                    "Nhận đề xuất dựa trên sở thích",

                icon =
                    Icons.Default.Favorite,

                checked =
                    autoSuggestions,

                onCheckedChange = {
                    autoSuggestions = it
                }
            )
        }

        item {

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(22.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            SurfaceWhite
                    )
            ) {

                Row(
                    modifier =
                        Modifier.padding(18.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier =
                            Modifier
                                .size(46.dp)
                                .background(
                                    SoftPurple,
                                    RoundedCornerShape(14.dp)
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Info,

                            contentDescription =
                                null,

                            tint =
                                PrimaryPurple
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(13.dp)
                    )

                    Column {

                        Text(
                            text =
                                "ALFM Library Management",

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "Phiên bản 1.0",

                            color =
                                TextSecondary,

                            fontSize =
                                11.sp
                        )
                    }
                }
            }
        }
    }
}


// ======================================================
// SETTINGS TITLE
// ======================================================

@Composable
private fun SettingsTitle(
    title: String
) {

    Text(
        text =
            title,

        fontSize =
            18.sp,

        fontWeight =
            FontWeight.ExtraBold,

        color =
            TextPrimary
    )
}


// ======================================================
// SETTINGS SWITCH
// ======================================================

@Composable
private fun PremiumSettingSwitch(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .shadow(
                    2.dp,
                    RoundedCornerShape(22.dp)
                ),

        shape =
            RoundedCornerShape(22.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    SurfaceWhite
            )
    ) {

        Row(
            modifier =
                Modifier.padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier =
                    Modifier
                        .size(46.dp)
                        .background(
                            SoftPurple,
                            RoundedCornerShape(14.dp)
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        icon,

                    contentDescription =
                        null,

                    tint =
                        PrimaryPurple
                )
            }

            Spacer(
                modifier =
                    Modifier.width(13.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        title,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        subtitle,

                    fontSize =
                        11.sp,

                    color =
                        TextSecondary
                )
            }

            Switch(
                checked =
                    checked,

                onCheckedChange =
                    onCheckedChange
            )
        }
    }
}
