package com.graltion.aquaclean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.graltion.aquaclean.ui.AquaCleanApp
import com.graltion.aquaclean.ui.theme.AquaCleanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AquaCleanTheme {
                AquaCleanApp()
            }
        }
    }
}
