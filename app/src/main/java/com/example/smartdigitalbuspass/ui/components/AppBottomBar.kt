package com.example.smartdigitalbuspass.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.smartdigitalbuspass.theme.RoyalBlue
import com.example.smartdigitalbuspass.ui.navigation.Screen

sealed class BottomNavItem(val title: String, val icon: ImageVector, val screen: Screen) {
    object Home : BottomNavItem("Home", AppIcons.Home, Screen.Home)
    object Routes : BottomNavItem("Routes", AppIcons.Bus, Screen.RouteTimings)
    object History : BottomNavItem("History", AppIcons.History, Screen.PassHistory)
    object Profile : BottomNavItem("Profile", AppIcons.Person, Screen.Profile)
}

@Composable
fun AppBottomBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Routes,
        BottomNavItem.History,
        BottomNavItem.Profile
    )

    NavigationBar(
        containerColor = Color.White,
        contentColor = RoyalBlue
    ) {
        items.forEach { item ->
            val selected = when (item) {
                is BottomNavItem.Home -> currentScreen is Screen.Home
                is BottomNavItem.Routes -> currentScreen is Screen.RouteTimings
                is BottomNavItem.History -> currentScreen is Screen.PassHistory
                is BottomNavItem.Profile -> currentScreen is Screen.Profile
            }

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.screen) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = RoyalBlue,
                    selectedTextColor = RoyalBlue,
                    indicatorColor = RoyalBlue.copy(alpha = 0.15f),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}
