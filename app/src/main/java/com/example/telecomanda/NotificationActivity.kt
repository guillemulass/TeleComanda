package com.example.telecomanda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.telecomanda.screens.notificationScreen.NotificationScreen
import com.example.telecomanda.ui.theme.TeleComandaTheme

class NotificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeleComandaTheme {
                val navController = rememberNavController()
                val notificationText = intent.getStringExtra("notification_text") ?: "No text"
                val tableNumber = intent.getStringExtra("notification_title") ?: "No text"


                NavHost(navController = navController, startDestination = "notification_screen") {
                    composable("notification_screen") {
                        NotificationScreen(navController = navController, text = notificationText, tableNumber = tableNumber)
                    }
                }
            }
        }
    }
}
