package com.example.notes.Screens

import androidx.compose.animation.AnimatedVisibility
import com.example.notes.CustomRegex.passwordIsValid
import com.example.notes.CustomRegex.emailIsValid
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.notes.utils.navigate.ScreensInfo
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun RegistrationScreen(navController: NavHostController = rememberNavController()) {
    val config = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidth = with(density) { config.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { config.screenHeightDp.dp.toPx() }
    var startAnimVisibleBox by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var valueEmail by remember { mutableStateOf("") }
    var valuePassword by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }
    var verticalScroll = rememberScrollState(0)
    var isEnd by remember { mutableStateOf(false) }

    var warningVisible = animateFloatAsState(
        targetValue = if (isEmailValid) 1.0f else 0.0f,
        animationSpec = tween(750)
    )

    var animVisibleText = animateFloatAsState(
        targetValue = if (startAnimVisibleBox) 1.0f else 0.0f,
        animationSpec = tween(3000)
    )

    val startX = animateFloatAsState(
        targetValue = if (isEnd) screenWidth else 0.0f,
        animationSpec = tween(1500)
    )

    val startY = animateFloatAsState(
        targetValue = if (isEnd) screenHeight else 0.0f,
        animationSpec = tween(3000)
    )

    val endX = animateFloatAsState(
        targetValue = if (isEnd) 0.0f else screenWidth,
        animationSpec = tween(1500)
    )

    val endY = animateFloatAsState(
        targetValue = if (isEnd) 0.0f else screenHeight,
        animationSpec = tween(3000)
    )



    LaunchedEffect(Unit) {
        startAnimVisibleBox = !startAnimVisibleBox
        while (true) {
            delay(3000)
            isEnd = !isEnd
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF121212), Color(0xFF1E1E1E)),
                    start = Offset(startX.value, startY.value),
                    end = Offset(endX.value, endY.value)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.weight(0.3f), verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.alpha(animVisibleText.value),
                    text = "Вас ждёт много интересного.",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier.alpha(animVisibleText.value),
                    text = "Зарегистрируйтесь!",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(modifier = Modifier.weight(0.7f), verticalAlignment = Alignment.Top) {
            Card(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .padding(10.dp)
                    .border(1.dp, Color(0xFFA919FF), shape = RoundedCornerShape(16.dp))
                    .verticalScroll(verticalScroll),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            ) {
                Column(modifier = Modifier
                    .alpha(warningVisible.value)
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                }

                AnimatedVisibility(visible = isEmailValid) {
                    Column(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        Text("Неккоректный email.",
                            modifier = Modifier.padding(start = 15.dp, top = 10.dp),
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                }

                OutlinedTextField(
                    value = valueEmail,
                    onValueChange = { newValue ->
                        valueEmail = newValue
                    },
                    placeholder = { Text("Email или номер телефона") },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (isEmailValid) 10.dp else 25.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp)),
                    textStyle = TextStyle(
                        fontSize = 15.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                        focusedContainerColor = Color(0xFF2A2A2A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xFFB0B0B0)
                    )
                )

                AnimatedVisibility(visible = isPasswordValid) {
                    Column(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start) {

                        Text("Минимум 6 цифр 1 заглавная буква и 1 спецсимвол.",
                            modifier = Modifier.padding(start = 15.dp, top = 10.dp),
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )   // Выравнивание по центру
                    }
                }

                OutlinedTextField(
                    value = valuePassword,
                    onValueChange = { newValue ->
                        valuePassword = newValue
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            val thisIcon =
                                if (passwordVisible) Icons.Default.Lock else Icons.Default.Check
                            Icon(thisIcon, contentDescription = "Description")
                        }
                    },
                    placeholder = { Text("Пароль") },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 25.dp, start = 10.dp, end = 10.dp)
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp)),
                    textStyle = TextStyle(
                        fontSize = 15.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                        focusedContainerColor = Color(0xFF2A2A2A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xFFB0B0B0)
                    )
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 15.dp)
                        .height(55.dp),
                    colors = ButtonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(14.dp),
                    onClick = {
                        isEmailValid = !emailIsValid(valueEmail)
                        isPasswordValid = !passwordIsValid(valuePassword)
                        if (!isEmailValid && !isPasswordValid) {
                            navController.navigate(ScreensInfo.MAIN_SCREEN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }) {
                    Text("Зарегестрироваться", fontSize = 20.sp)
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row() {
                        Text(
                            "Есть аккаунт? ",
                            color = Color.White,
                            fontSize = 14.sp
                        )

                        Text("Войти",
                            Modifier
                                .padding(bottom = 15.dp)
                                .clickable(onClick = { navController.navigate(ScreensInfo.Companion.LOGIN_SCREEN) }),
                            color = Color.White,
                            fontSize = 14.sp,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }
    }
}