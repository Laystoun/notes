package com.example.notes.utils.navigate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notes.Screens.LoadingScreen
import com.example.notes.Screens.LogInScreen
import com.example.notes.Screens.MainScreen
import com.example.notes.Screens.RegOrGuestScreen
import com.example.notes.Screens.RegistrationScreen
import com.example.notes.utils.datastore.DataStoreConfiguration
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val store = DataStoreConfiguration(LocalContext.current)
            var startDestination by remember { mutableStateOf<String?>(null) }
            var isStartApp by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                val item = store.getValueGuestConf().first()
                startDestination = if (item.showChoice) {
                    ScreensInfo.REG_OR_GUEST_SCREEN
                } else {
                    ScreensInfo.MAIN_SCREEN
                }

//                delay(1800)
                isStartApp = false
            }

            if (isStartApp) {
                LoadingScreen()
            } else if (startDestination != null) {
                NavHost(navController = navController, startDestination = startDestination!!) {
                    composable(ScreensInfo.REG_OR_GUEST_SCREEN) {
                        RegOrGuestScreen(navController)
                    }
                    composable(ScreensInfo.LOGIN_SCREEN) {
                        LogInScreen(navController)
                    }
                    composable(ScreensInfo.REGISTRATION_SCREEN) {
                        RegistrationScreen(navController)
                    }
                    composable(ScreensInfo.MAIN_SCREEN) {
                        MainScreen()
                    }
                }
            }
        }
    }
}