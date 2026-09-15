package com.example.myapplication1.ui.theme.DashBoard

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    onBackClick: () -> Unit,
    onSave: (
        title: String,
        author: String,
        imageUrl: String?,
        publishYear: Int?,
        genre: String?
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var publishYear by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val pageBackground = Color(0xFFF7F7FB)
    val textPrimary = Color(0xFF24232D)
    val textSecondary = Color(0xFF777582)
    val softPurple = Color(0xFFF0EBFF)

    Scaffold(
        containerColor = pageBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Thêm sách",
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryPurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(PrimaryPurple, Color(0xFF5840B8))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(58.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = Color.White.copy(alpha = 0.16f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(31.dp)
                                )
                            }
                        }

                        Spacer(Modifier.width(14.dp))

                        Column {
                            Text(
                                "Thêm sách mới",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                "Điền thông tin cơ bản của cuốn sách",
                                color = Color.White.copy(alpha = 0.78f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(42.dp),
                            shape = CircleShape,
                            color = softPurple
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.AutoStories,
                                    null,
                                    tint = PrimaryPurple,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(11.dp))
                        Column {
                            Text(
                                "Thông tin sách",
                                color = textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                            Text(
                                "Tên sách và tác giả là bắt buộc",
                                color = textSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFEEEEF3))

                    PrettyBookField(
                        title,
                        { title = it; errorMessage = "" },
                        "Tiêu đề *",
                        "Nhập tên sách"
                    )
                    PrettyBookField(
                        author,
                        { author = it; errorMessage = "" },
                        "Tác giả *",
                        "Nhập tên tác giả"
                    )
                    PrettyBookField(
                        genre,
                        { genre = it },
                        "Thể loại",
                        "Ví dụ: Tiểu thuyết"
                    )
                    PrettyBookField(
                        publishYear,
                        {
                            publishYear = it.filter(Char::isDigit)
                        },
                        "Năm xuất bản",
                        "Ví dụ: 2024",
                        KeyboardType.Number
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(42.dp),
                            shape = CircleShape,
                            color = softPurple
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Image,
                                    null,
                                    tint = PrimaryPurple,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(11.dp))
                        Column {
                            Text(
                                "Ảnh bìa",
                                color = textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                            Text(
                                "Chọn ảnh trực tiếp từ thư viện máy",
                                color = textSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFEEEEF3))

                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Ảnh bìa sách",
                            modifier = Modifier
                                .size(width = 150.dp, height = 205.dp)
                                .clip(RoundedCornerShape(18.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(width = 150.dp, height = 190.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(softPurple)
                                .clickable { imagePicker.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Image,
                                    null,
                                    tint = PrimaryPurple,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "Chưa chọn ảnh",
                                    color = textSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { imagePicker.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = softPurple,
                            contentColor = PrimaryPurple
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (selectedImageUri == null) "Chọn ảnh từ máy" else "Đổi ảnh",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (errorMessage.isNotBlank()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    color = Color(0xFFFFECEC)
                ) {
                    Text(
                        errorMessage,
                        modifier = Modifier.padding(13.dp),
                        color = Color(0xFFB3261E),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Button(
                onClick = {
                    when {
                        title.isBlank() ->
                            errorMessage = "Vui lòng nhập tiêu đề"

                        author.isBlank() ->
                            errorMessage = "Vui lòng nhập tác giả"

                        else -> onSave(
                            title.trim(),
                            author.trim(),
                            selectedImageUri?.toString(),
                            publishYear.toIntOrNull(),
                            genre.trim().ifBlank { null }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple
                )
            ) {
                Text(
                    "Lưu sách",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(Modifier.height(15.dp))
        }
    }
}

@Composable
private fun PrettyBookField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(16.dp)
    )
}
