package com.example.smartdigitalbuspass.ui.navigation

sealed class Screen {
    object Splash : Screen()
    object LoginRegister : Screen()
    object Home : Screen()
    object BusPassInfo : Screen()
    data class ApplyPass(val selectedRouteId: Long? = null) : Screen()
    data class DigitalPass(val passId: String) : Screen()
    object RouteTimings : Screen()
    object PassHistory : Screen()
    object Profile : Screen()
    object AboutHelp : Screen()
}
