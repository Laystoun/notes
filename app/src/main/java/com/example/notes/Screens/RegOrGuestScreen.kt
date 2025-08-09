package com.example.notes.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notes.utils.navigate.ScreensInfo
import com.example.notes.utils.datastore.DataStoreConfiguration
import com.example.notes.utils.datastore.isGuest.GuestOrRegistration
import kotlinx.coroutines.launch

@Composable
fun RegOrGuestScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val store = DataStoreConfiguration(LocalContext.current)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF121212), Color(0xFF1E1E1E))
                )
            )
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(
                    start = 10.dp,
                    end = 10.dp
                )
                .border(2.dp, Color(0xFFA919FF), shape = RoundedCornerShape(16.dp)),
            colors = CardColors(
                containerColor = Color(0xFF231D1D),
                contentColor = Color.Transparent,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.background(Color.Transparent)
            ) {

                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 15.dp, top = 15.dp)
                    .height(55.dp),
                    colors = ButtonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(14.dp),
                    onClick = {
                        navController.navigate(ScreensInfo.MAIN_SCREEN)
                        scope.launch {
                            store.setValueGuestConf(
                                GuestOrRegistration(true, showChoice = false)
                            )
                        }
                    },
                ) {
                    Text("Без аккаунта", fontSize = 20.sp)
                }

                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 15.dp, top = 15.dp)
                    .height(55.dp),
                    colors = ButtonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(14.dp),
                    onClick = {
                        navController.navigate(ScreensInfo.REGISTRATION_SCREEN)
                    }
                ) {
                    Text("Создать аккаунт", fontSize = 20.sp)
                }
            }
        }
    }
}