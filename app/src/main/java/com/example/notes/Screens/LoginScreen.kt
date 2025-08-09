package com.example.notes.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.notes.utils.navigate.ScreensInfo
import com.example.notes.CustomRegex.emailIsValid
import kotlinx.coroutines.delay
import com.example.notes.CustomRegex.passwordIsValid

@Preview(showBackground = true)
@Composable
fun LogInScreen(navController: NavHostController = rememberNavController()) {
    var valueEmail by remember { mutableStateOf("") }   // Поле email
    var valuePassword by remember { mutableStateOf("") }   // Поле password
    var isEmailValid by remember { mutableStateOf(false) }  // верная ли почта? ( логическая )
    var isPasswordValid by remember { mutableStateOf(false) } // верный ли пароль? ( логическая )
    var passwordVisible by remember { mutableStateOf(false) }   // Скрыть пароль или нет ( логическая )
    var isFloatStart by remember { mutableStateOf(false) }  //  Закончилась ли анимация ( переливка цветов )
    val verticalScroll = rememberScrollState(0)     // Вертикальный скролл Card для горизонатльного экрана
    var startAnimVisibleBox by remember { mutableStateOf(false) }   // Начинать ли анимацию появления верхней надписи ( логическая )
    val config = LocalConfiguration.current   // Параметры экрана телефона ( подтягиваем разрешение )
    val density = LocalDensity.current  // Операции с переводом в размерности ( пиксели/dp и т.д. )
    val localWidth = with (density) { config.screenWidthDp.dp.toPx() }  // ширина в пикселях
    val localHeight = with (density) { config.screenHeightDp.dp.toPx() } // высота в пикселях

    val animVisibleText = animateFloatAsState(      // Анимация появления верхней надписи
        targetValue = if (startAnimVisibleBox) 1.0f else 0.0f,
        animationSpec = tween(3000)
    )

    val startX = animateFloatAsState(   // перелив верхнего по оси X
        targetValue = if (isFloatStart) localWidth else 0f,
        animationSpec = tween(1500)
    )

    val startY = animateFloatAsState(   // перелив верхнего по оси Y
        targetValue = if (isFloatStart) localHeight else 0f,
        animationSpec = tween(3000)
    )

    val endX = animateFloatAsState(     // перелив нижнего по оси X
        targetValue = if (isFloatStart) 0f else localWidth,
        animationSpec = tween(1500)
    )

    var endY = animateFloatAsState(     // перелив нижнего по оси Y
        targetValue = if (isFloatStart) 0f else localHeight,
        animationSpec = tween(3000)
    )

    LaunchedEffect(Unit) {          // Асинхронная функция
        startAnimVisibleBox = !startAnimVisibleBox  // начинаем анимацию появления надписи сверху
        while(true) {   // бесконечный перелив цветами
            delay(3000)
            isFloatStart = !isFloatStart
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
        Row(modifier = Modifier
            .weight(0.3f)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Рады видеть вас снова!",
                    modifier = Modifier.alpha(animVisibleText.value),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold, // Жирность шрифта
                    overflow = TextOverflow.Ellipsis)   // Что делать если текст переполнит экран


                Text("Давайте продолжим.",
                    modifier = Modifier
                        .alpha(animVisibleText.value),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold, // Жирность шрифта
                    overflow = TextOverflow.Ellipsis)   // Что делать если текст переполнит экран
            }
        }

        Row(modifier = Modifier
            .fillMaxHeight()
            .weight(0.7f),
            verticalAlignment = Alignment.Top) {

            Column() {
                Card(modifier = Modifier
                    .widthIn(max = 400.dp)
                    .padding(10.dp)
                    .verticalScroll(verticalScroll)
                    .border(1.dp, Color(0xFFA919FF), shape = RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A1A1A)
                    )
                ) {

                    Column(modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        AnimatedVisibility(visible = isEmailValid) {
                            Column(modifier = Modifier
                                .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally) {

                                Text("Неккоректный email.",
                                    modifier = Modifier.padding(top = 10.dp),
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        OutlinedTextField(      // Текстовое поле для Email
                            value = valueEmail,
                            onValueChange = {
                                    newValue -> valueEmail = newValue
                            },
                            placeholder = {Text("Email или номер телефона")},
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
                                focusedContainerColor = Color(0xFF1A1A1A),
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
                            onValueChange = {
                                    newValue -> valuePassword = newValue
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = {passwordVisible = !passwordVisible}) {
                                    val thisIcon = if (passwordVisible) Icons.Default.Lock else Icons.Default.Check
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
                                focusedContainerColor = Color(0xFF1A1A1A),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color(0xFFB0B0B0)
                            )
                        )
                    }

                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                        Text("Забыли пароль?", modifier = Modifier
                            .padding(bottom = 10.dp, end = 15.dp),
                            color = Color.White)
                    }

                    Column() {
                        Button(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 15.dp)
                            .height(55.dp),
                            colors = ButtonColors(
                                containerColor = Color.Blue,
                                contentColor = Color.White,
                                disabledContentColor = Color.Black,
                                disabledContainerColor = Color.LightGray
                            ),
                            shape = RoundedCornerShape(14.dp), onClick = {
                                isEmailValid = !emailIsValid(valueEmail) // проверка валидности почты
                                isPasswordValid = !passwordIsValid(valuePassword)
                                if (!isEmailValid && !isPasswordValid) {
                                    navController.navigate(ScreensInfo.MAIN_SCREEN) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            } ) {
                            Text("Войти", fontSize = 20.sp)
                        }

                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Зарегестрироваться",
                                Modifier
                                    .padding(bottom = 15.dp)
                                    .clickable(onClick = {navController.navigate(ScreensInfo.Companion.REGISTRATION_SCREEN)}),
                                color = Color.White,
                                fontSize = 14.sp,
                                textDecoration = TextDecoration.Underline)
                        }
                    }
                }
            }
        }
    }
}