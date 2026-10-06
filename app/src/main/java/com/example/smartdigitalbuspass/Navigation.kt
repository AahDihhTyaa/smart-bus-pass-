package com.example.smartdigitalbuspass

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.smartdigitalbuspass.data.DatabaseHelper
import com.example.smartdigitalbuspass.data.SessionManager
import com.example.smartdigitalbuspass.ui.navigation.Screen
import com.example.smartdigitalbuspass.ui.screens.*

@Composable
fun MainNavigation() {
    val context = LocalContext.current
    val dbHelper = remember { DatabaseHelper(context) }
    val sessionManager = remember { SessionManager(context) }

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }

    Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
        when (screen) {
            is Screen.Splash -> SplashScreen(
                sessionManager = sessionManager,
                onNavigate = { currentScreen = it }
            )
            is Screen.LoginRegister -> LoginRegisterScreen(
                dbHelper = dbHelper,
                sessionManager = sessionManager,
                onNavigate = { currentScreen = it }
            )
            is Screen.Home -> HomeScreen(
                dbHelper = dbHelper,
                sessionManager = sessionManager,
                onNavigate = { currentScreen = it }
            )
            is Screen.BusPassInfo -> BusPassInfoScreen(
                onNavigate = { currentScreen = it }
            )
            is Screen.ApplyPass -> ApplyPassScreen(
                initialRouteId = screen.selectedRouteId,
                dbHelper = dbHelper,
                sessionManager = sessionManager,
                onNavigate = { currentScreen = it }
            )
            is Screen.DigitalPass -> DigitalPassScreen(
                passId = screen.passId,
                dbHelper = dbHelper,
                sessionManager = sessionManager,
                onNavigate = { currentScreen = it }
            )
            is Screen.RouteTimings -> RouteTimingsScreen(
                dbHelper = dbHelper,
                sessionManager = sessionManager,
                onNavigate = { currentScreen = it }
            )
            is Screen.PassHistory -> PassHistoryScreen(
                dbHelper = dbHelper,
                sessionManager = sessionManager,
                onNavigate = { currentScreen = it }
            )
            is Screen.Profile -> ProfileScreen(
                dbHelper = dbHelper,
                sessionManager = sessionManager,
                onNavigate = { currentScreen = it }
            )
            is Screen.AboutHelp -> AboutHelpScreen(
                onNavigate = { currentScreen = it }
            )
        }
    }
}
