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

    when (currentScreen) {

        "login" -> {
            LoginScreen(
                onRegisterClick = {
                    currentScreen = "register"
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
    }
}


// ======================================================
// LOGIN SCREEN
// ======================================================

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit
) {

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

                // LOGO
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

                // EMAIL
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

                // PASSWORD
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
                            if (password.isNotEmpty()) {
                                message = "Bạn đã nhập mật khẩu"
                            } else {
                                message = "Vui lòng nhập mật khẩu"
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

                // QUÊN MẬT KHẨU
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

                // ĐĂNG NHẬP
                Button(
                    onClick = {

                        message =
                            when {
                                email.isBlank() ->
                                    "Vui lòng nhập email"

                                !isValidEmail(email) ->
                                    "Email không hợp lệ"

                                password.isBlank() ->
                                    "Vui lòng nhập mật khẩu"

                                else ->
                                    "Đăng nhập thành công"
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
                        text = "Đăng nhập",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // ĐĂNG KÝ
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

                // MESSAGE
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

                    // LOGO
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

                    // HỌ VÀ TÊN
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

                    // EMAIL
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

                    // SỐ ĐIỆN THOẠI
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

                    // MẬT KHẨU
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

                    // XÁC NHẬN MẬT KHẨU
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
                                if (password == confirmPassword) {
                                    message = "Mật khẩu đã khớp"
                                } else {
                                    message = "Mật khẩu chưa khớp"
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

                    // ĐĂNG KÝ
                    Button(
                        onClick = {

                            message =
                                when {

                                    fullName.isBlank() ->
                                        "Vui lòng nhập họ và tên"

                                    email.isBlank() ->
                                        "Vui lòng nhập email"

                                    !isValidEmail(email) ->
                                        "Email không hợp lệ"

                                    phone.isBlank() ->
                                        "Vui lòng nhập số điện thoại"

                                    phone.length < 10 ->
                                        "Số điện thoại không hợp lệ"

                                    password.isBlank() ->
                                        "Vui lòng nhập mật khẩu"

                                    password.length < 6 ->
                                        "Mật khẩu phải có ít nhất 6 ký tự"

                                    confirmPassword.isBlank() ->
                                        "Vui lòng xác nhận mật khẩu"

                                    password != confirmPassword ->
                                        "Mật khẩu xác nhận không khớp"

                                    else ->
                                        "Đăng ký thành công!"
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
                            text = "Đăng ký",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // MESSAGE
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

                    // QUAY LẠI LOGIN
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