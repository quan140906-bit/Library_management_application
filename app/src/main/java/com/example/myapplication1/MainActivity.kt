package com.example.myapplication1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication1.data.BookRepository
import com.example.myapplication1.data.Book
import androidx.compose.runtime.LaunchedEffect
import com.example.myapplication1.ui.theme.DashBoard.AddBookScreen
import com.example.myapplication1.ui.theme.DashBoard.InventoryManagementScreen
import kotlinx.coroutines.launch
import com.example.myapplication1.data.AuthRepository
import androidx.compose.runtime.rememberCoroutineScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ALFMApp()
        }
    }
}


// ======================================================
// ALFM APP
// ======================================================

@Composable
fun ALFMApp() {

    var currentScreen by remember {
        mutableStateOf("login")
    }

    val repository = remember { BookRepository() }
    var books by remember { mutableStateOf(emptyList<Book>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentScreen) {
        try {
            books = repository.getAllBooks()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    when (currentScreen) {

        "login" -> {
            LoginScreen(
                onRegisterClick = {
                    currentScreen = "register"
                },
                onLoginSuccess = {
                    currentScreen = "library"
                }
            )
        }

        "register" -> {
            RegisterScreen(
                onLoginClick = {
                    currentScreen = "login"
                }
            )
        }

        "library" -> {
            InventoryManagementScreen(
                books = books,
                onMarkAsRead = { bookId ->
                    scope.launch {
                        repository.markAsRead(bookId)
                        books = repository.getAllBooks()
                    }
                },
                onNavigateToAddBook = {
                    currentScreen = "add_book"
                }
            )
        }

        "add_book" -> {
            AddBookScreen(
                onBackClick = {
                    currentScreen = "library"
                },
                onSave = { title, author, imageUrl, publishYear, genre ->
                    scope.launch {
                        repository.addBook(title, author, imageUrl, publishYear, genre)
                        books = repository.getAllBooks()
                        currentScreen = "library"
                    }
                }
            )
        }
    }
}


// ======================================================
// LOGIN SCREEN
// ======================================================

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {

    val authRepository = remember { AuthRepository() }
    val scope = rememberCoroutineScope()

    var isLoading by remember {
        mutableStateOf(false)
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf("")
    }

    val primary = Color(0xFF6750A4)

    val background = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF4F0FF),
            Color(0xFFF9F8FC),
            Color.White
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 400.dp)
                .padding(horizontal = 22.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "📚",
                    fontSize = 48.sp
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Welcome to ALFM",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "Library Management",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(30.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        message = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Email")
                    },
                    placeholder = {
                        Text("example@gmail.com")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        message = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Mật khẩu")
                    },
                    placeholder = {
                        Text("Nhập mật khẩu")
                    },
                    singleLine = true,
                    visualTransformation =
                        if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),

                    keyboardActions = KeyboardActions(
                        onDone = {
                            message = if (password.isNotEmpty()) {
                                "Bạn đã nhập mật khẩu"
                            } else {
                                "Vui lòng nhập mật khẩu"
                            }
                        }
                    ),

                    trailingIcon = {
                        TextButton(
                            onClick = {
                                passwordVisible = !passwordVisible
                            }
                        ) {
                            Text(
                                text =
                                    if (passwordVisible)
                                        "Ẩn"
                                    else
                                        "Hiện",
                                color = primary,
                                fontSize = 12.sp
                            )
                        }
                    },

                    shape = RoundedCornerShape(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
                        onClick = {
                            message = "Chức năng quên mật khẩu"
                        }
                    ) {

                        Text(
                            text = "Quên mật khẩu?",
                            color = primary,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = {

                        when {
                            email.isBlank() -> {
                                message = "Vui lòng nhập email"
                            }

                            !isValidEmail(email) -> {
                                message = "Email không hợp lệ"
                            }

                            password.isBlank() -> {
                                message = "Vui lòng nhập mật khẩu"
                            }

                            else -> {

                                scope.launch {

                                    isLoading = true
                                    message = ""

                                    val result = authRepository.login(
                                        email = email,
                                        password = password
                                    )

                                    isLoading = false

                                    result
                                        .onSuccess { response ->

                                            message = "Đăng nhập thành công!"

                                            // Login thành công
                                            onLoginSuccess()
                                        }
                                        .onFailure { error ->

                                            message =
                                                error.message
                                                    ?: "Đăng nhập thất bại"
                                        }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primary
                    )
                ) {
                    Text(
                        text = if (isLoading)
                            "Đang đăng nhập..."
                        else
                            "Đăng nhập",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Chưa có tài khoản?",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    Spacer(
                        modifier = Modifier.width(2.dp)
                    )

                    TextButton(
                        onClick = onRegisterClick
                    ) {

                        Text(
                            text = "Đăng ký",
                            color = primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (message.isNotEmpty()) {

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text = message,
                        color = primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


// ======================================================
// REGISTER SCREEN
// ======================================================

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit
) {
    val authRepository = remember { AuthRepository() }
    val scope = rememberCoroutineScope()

    var isLoading by remember {
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

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf("")
    }

    val primary = Color(0xFF6750A4)

    val background = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF4F0FF),
            Color(0xFFF9F8FC),
            Color.White
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 22.dp,
                    vertical = 30.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 400.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "📚",
                        fontSize = 44.sp
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = "Create Account",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF222222)
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text = "Join ALFM Library",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(
                        modifier = Modifier.height(25.dp)
                    )

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            message = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Họ và tên")
                        },
                        placeholder = {
                            Text("Nguyen Van A")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(15.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(13.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            message = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Email")
                        },
                        placeholder = {
                            Text("abcxyz@gmail.com")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(15.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(13.dp)
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            phone = it.filter { char ->
                                char.isDigit()
                            }
                            message = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Số điện thoại")
                        },
                        placeholder = {
                            Text("09xxxxxxxx")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(15.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(13.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            message = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Mật khẩu")
                        },
                        placeholder = {
                            Text("Tối thiểu 6 ký tự")
                        },
                        singleLine = true,
                        visualTransformation =
                            if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),

                        trailingIcon = {

                            TextButton(
                                onClick = {
                                    passwordVisible =
                                        !passwordVisible
                                }
                            ) {

                                Text(
                                    text =
                                        if (passwordVisible)
                                            "Ẩn"
                                        else
                                            "Hiện",
                                    color = primary,
                                    fontSize = 12.sp
                                )
                            }
                        },

                        shape = RoundedCornerShape(15.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(13.dp)
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            message = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Xác nhận mật khẩu")
                        },
                        placeholder = {
                            Text("Nhập lại mật khẩu")
                        },
                        singleLine = true,
                        visualTransformation =
                            if (confirmPasswordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),

                        keyboardActions = KeyboardActions(
                            onDone = {
                                message = if (password == confirmPassword) {
                                    "Mật khẩu đã khớp"
                                } else {
                                    "Mật khẩu chưa khớp"
                                }
                            }
                        ),

                        trailingIcon = {

                            TextButton(
                                onClick = {
                                    confirmPasswordVisible =
                                        !confirmPasswordVisible
                                }
                            ) {

                                Text(
                                    text =
                                        if (confirmPasswordVisible)
                                            "Ẩn"
                                        else
                                            "Hiện",
                                    color = primary,
                                    fontSize = 12.sp
                                )
                            }
                        },

                        shape = RoundedCornerShape(15.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    Button(
                        onClick = {

                            when {

                                fullName.isBlank() -> {
                                    message = "Vui lòng nhập họ và tên"
                                }

                                email.isBlank() -> {
                                    message = "Vui lòng nhập email"
                                }

                                !isValidEmail(email) -> {
                                    message = "Email không hợp lệ"
                                }

                                phone.isBlank() -> {
                                    message = "Vui lòng nhập số điện thoại"
                                }

                                phone.length < 10 -> {
                                    message = "Số điện thoại không hợp lệ"
                                }

                                password.isBlank() -> {
                                    message = "Vui lòng nhập mật khẩu"
                                }

                                password.length < 6 -> {
                                    message = "Mật khẩu phải có ít nhất 6 ký tự"
                                }

                                confirmPassword.isBlank() -> {
                                    message = "Vui lòng xác nhận mật khẩu"
                                }

                                password != confirmPassword -> {
                                    message = "Mật khẩu xác nhận không khớp"
                                }

                                else -> {

                                    scope.launch {

                                        isLoading = true
                                        message = ""

                                        val result = authRepository.register(
                                            fullName = fullName,
                                            email = email,
                                            phone = phone,
                                            password = password
                                        )

                                        isLoading = false

                                        result
                                            .onSuccess {

                                                message =
                                                    "Đăng ký thành công!"

                                                // Chuyển về Login
                                                onLoginClick()
                                            }
                                            .onFailure { error ->

                                                message =
                                                    error.message
                                                        ?: "Đăng ký thất bại"
                                            }
                                    }
                                }
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primary
                        )
                    ) {

                        Text(
                            text = if (isLoading)
                                "Đang đăng ký..."
                            else
                                "Đăng ký",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (message.isNotEmpty()) {

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = message,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = primary
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Đã có tài khoản?",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )

                        TextButton(
                            onClick = onLoginClick
                        ) {

                            Text(
                                text = "Đăng nhập",
                                color = primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


// ======================================================
// KIỂM TRA EMAIL
// ======================================================

fun isValidEmail(email: String): Boolean {

    return android.util.Patterns
        .EMAIL_ADDRESS
        .matcher(email)
        .matches()
}