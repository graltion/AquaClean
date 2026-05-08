package com.graltion.aquaclean.ui

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Main : Screen("main")
    object Cleaning : Screen("cleaning/{mode}/{duration}/{deviceType}") {
        fun createRoute(mode: String, duration: Int, deviceType: String) =
            "cleaning/$mode/$duration/$deviceType"
    }
    object Completion : Screen("completion/{mode}/{duration}") {
        fun createRoute(mode: String, duration: Int) =
            "completion/$mode/$duration"
    }
    object Settings : Screen("settings")
    object About : Screen("about")
    object Terms : Screen("terms")
    object Privacy : Screen("privacy")
    object Licenses : Screen("licenses")
}
